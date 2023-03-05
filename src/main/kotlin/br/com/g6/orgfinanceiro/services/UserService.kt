package br.com.g6.orgfinanceiro.services

import br.com.g6.orgfinanceiro.dto.NewPassword
import br.com.g6.orgfinanceiro.model.Users
import br.com.g6.orgfinanceiro.model.UsersLogin
import br.com.g6.orgfinanceiro.repository.UserRepository
import com.sun.mail.smtp.SMTPSendFailedException
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.util.*
import kotlin.jvm.Throws

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var enconder : BCryptPasswordEncoder

    @Autowired
    private lateinit var emailService: SpringMailService;

    fun save(user: Users): Users {
        user.password = enconder.encode(user.password)
        var erroEmail: String = ""
        try {
            if(userRepository.findByEmail(user.email) == null){
                var body: String = "Olá ${user.name}! Seja bem-vindo(a) ao Meu Boleto Pago.\n" +
                        "O controle das suas finanças na palma da sua mão."
                emailService.sendEmail(
                    user.email,
                    "Bem-vindo(a) ao Meu Boleto Pago!",
                    body
                )
            }
        } catch (ex: SMTPSendFailedException){
            erroEmail = "\nNão foi possível confirmar seu email. Por favor, verifique o seu email e atualize seus dados."
            ex.message + erroEmail
        }
        return userRepository.save(user)
    }


//    public Optional<UserLogin> logar(Optional<UserLogin> userLogin) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        Optional<User> user = userRepository.findByUserEmail(userLogin.get().getUserName());


    fun login(usersLogin: UsersLogin?): UsersLogin? {
        var user : Users? = userRepository.findByEmail(usersLogin?.email)
        if(user != null && usersLogin != null){
            if(enconder.matches(usersLogin.password, user.password)){
                var auth: String = usersLogin.email + ":" + usersLogin.password
                var encodeAuth: ByteArray = Base64.encodeBase64(auth.toByteArray(Charset.forName("US-ASCII")))
                var authHeader: String = "Basic " + String(encodeAuth)
                usersLogin.email = user.email
                usersLogin.password = user.password
                usersLogin.token = authHeader
                usersLogin.id = user.id!!

                return usersLogin

            }
        }
        return null
    }

    fun createRecoveryToken(user: Users) {
        val token: String = UUID.randomUUID().toString()
        user.recoveryToken = token.substring(0,20)
        userRepository.save(user)
        emailService.sendEmail(toEMail = user.email,
                                subject = "Recuperação de senha",
                                body = "Olá, ${user.name}\n" +
                                        "Seu token de recuperação de senha é: ${user.recoveryToken}")
    }
    @Throws(Exception::class)
    fun changePassword(newPassword: NewPassword): Users? {
        val user : Users? = userRepository.findByRecoveryToken(newPassword.token)
        if (user != null){
            user.recoveryToken = user.let {
                it.password = enconder.encode(newPassword.password)
                null
            }
            emailService.sendEmail(toEMail = user.email,
                subject = "Recuperação de senha",
                body = "Olá, ${user.name}\n" +
                        "Sua senha foi atualizada com sucesso!")
        }
        return user
    }


}