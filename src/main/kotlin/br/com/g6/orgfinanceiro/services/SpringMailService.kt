package br.com.g6.orgfinanceiro.services

import com.sun.mail.smtp.SMTPSendFailedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class SpringMailService {
    @Autowired
    lateinit var emailSender: JavaMailSender;

    @Throws(SMTPSendFailedException::class)
    fun sendEmail(toEMail: String, subject: String, body: String){
        val message: SimpleMailMessage = SimpleMailMessage()
        message.run {
            setFrom("meu.boleto.pago.g6@gmail.com")
            setTo(toEMail)
            setText(body)
            setSubject(subject)
        }
        emailSender.send(message)

    }


}