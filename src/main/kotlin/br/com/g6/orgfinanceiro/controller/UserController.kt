package br.com.g6.orgfinanceiro.controller

import br.com.g6.orgfinanceiro.dto.EmailDTO
import br.com.g6.orgfinanceiro.dto.NewPassword
import br.com.g6.orgfinanceiro.dto.UsersDTO
import br.com.g6.orgfinanceiro.repository.UserRepository
import br.com.g6.orgfinanceiro.model.Users
import br.com.g6.orgfinanceiro.model.UsersLogin
import br.com.g6.orgfinanceiro.services.SpringMailService
import br.com.g6.orgfinanceiro.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

//porta de entrada da aplicação, onde chegam as requisições
//anotações e recursos
@RestController
@RequestMapping("/user")
//parametros do construtor: entender que a interface é um Bean e fazer a injeção de dependência
class UserController(private val repository: UserRepository) {

    @Autowired
    private lateinit var userService : UserService;

    @Autowired
    private lateinit var emailService: SpringMailService;

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder;

//    @Autowired
//    private lateinit var usersLogin: UsersLogin


    //receber requisição como método http post
    @PostMapping("/signup")
    //quando tiver request tem que ter a response entity?
    fun create(@RequestBody @Valid usersDTO: UsersDTO) : ResponseEntity<Users> {
        var user : Users = Users(null, usersDTO.name, usersDTO.email, usersDTO.password)

        return ResponseEntity.ok(userService.save(user))
    }

    //--ERRO: a pessoa só consegue se logar uma evz
    @PostMapping("/login")
    fun authentication (@RequestBody user: UsersLogin?) : ResponseEntity<UsersLogin> {
       var user = userService.login(user)
        return  ResponseEntity.ok(user!!)


    }


    //retorna uma response entity com uma lista de accounts
    @GetMapping("/users")
    fun read() : ResponseEntity<MutableList<Users>> = ResponseEntity.ok(repository.findAll())


    @GetMapping(value = ["/{userId}"])
    fun getById(@PathVariable userId: Long) : ResponseEntity<Users?> =
        repository.findById(userId).map {
            ResponseEntity.ok(it) // ponteiro/this
        }.orElse(ResponseEntity.notFound().build())
    //RETORNAR RESPOSTA PARA O USUÁRIO NÃO ENCONTRADO OU NÃO PERMITIDO
    

    @PutMapping(value = ["/{userId}"])
    fun update (@PathVariable userId: Long, @RequestBody user: Users) : ResponseEntity<Users> {
        //ver se o usuário existe - se sim, coloca o retorna na variável
        val userUpdateOptional = repository.findById(userId) //or if( !userUpdateOtiona.isPresent)
        val userUpdateSave = userUpdateOptional
            .orElseThrow { RuntimeException("User $userId not found!")}
            .copy(name = user.name, email = user.email, password = encoder.encode(user.password))
            .also {
                if (userUpdateOptional.get().email != user.email){
                    emailService.sendEmail(user.email,
                        "Email alterado.",
                        "${user.name}, seu email foi alterado com sucesso!")
                }
            }
        return ResponseEntity.ok(repository.save(userUpdateSave))

        }
//            repository.userUpdate(it)



    @DeleteMapping(value = ["/{userId}"])
    fun delete (@PathVariable userId: Long) : Unit =
        repository
            .findById(userId)
            .ifPresent{

                ResponseEntity.ok("Usuário deletado com sucesso!")
                (repository.delete(it))}


    @PostMapping("/forgot-password")
    fun sendToken (@RequestBody @Valid emailDTO: EmailDTO) : ResponseEntity<Any>{
        val user = repository.findByEmail(emailDTO.email)
        user?.let {
            userService.createRecoveryToken(it)
            return ResponseEntity.ok("Olá ${it.name}, o token de recuperação foi enviado ao seu e-mail.")
        }
        return ResponseEntity.notFound().build()
    }



    @PostMapping("/password-recovery")
    fun changePassword(@RequestBody @Valid newPassword: NewPassword): ResponseEntity<Any>{
        val user: Users? = userService.changePassword(newPassword)
        if (user != null){
            repository.save(user)
            return ResponseEntity.ok("Senha atualizada com sucesso! Faça o login com sua nova senha.")
        }
        return ResponseEntity.notFound().build()
    }
}

