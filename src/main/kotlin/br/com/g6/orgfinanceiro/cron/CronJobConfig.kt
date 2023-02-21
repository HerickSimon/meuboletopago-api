package br.com.g6.orgfinanceiro.cron

import br.com.g6.orgfinanceiro.dto.MovementDTO
import br.com.g6.orgfinanceiro.enumeration.TypeMovement
import br.com.g6.orgfinanceiro.model.Movement
import br.com.g6.orgfinanceiro.repository.MovementRepository
import br.com.g6.orgfinanceiro.services.CurrentUserService
import br.com.g6.orgfinanceiro.services.FilterMovementSpecification
import br.com.g6.orgfinanceiro.services.SpringMailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import java.time.LocalDate
import java.time.Period
import java.util.*


@Configuration
@EnableScheduling // Habilita agendamento
class CronJobConfig() {

    @Autowired
    lateinit var repository: MovementRepository

    @Autowired
    lateinit var emailService: SpringMailService;

    @Autowired
    private val currentUserService: CurrentUserService? = null

    //second, minute, hour, day, month, weekday
    //@Scheduled(cron = "* * 8 * * ?")
    // AGENDAMENTO
    @Scheduled(cron = "0/30 * * * * ?")
    fun sendMail() {
        expenseDueDate() // Conta a vencer
        overdueAccount() // Conta Vencida
    }
    fun expenseDueDate() {
        var dto = MovementDTO().apply {
            wasPaid = false
            typeMovement = TypeMovement.DESPESA.toString()
            var dataFilter = LocalDate.now().plusDays(1)
            dueDateIni = dataFilter
            dueDateEnd = dataFilter
        }

        var filterMovement = FilterMovementSpecification(dto)
        var listMovements = repository.findAll(filterMovement)

        val mapUser: Map<Long?, List<Movement>> = listMovements.groupBy { it.user?.id }

        if(mapUser.isNotEmpty()) {
            for (listMov in mapUser.values) {
                val usuario = listMov[0].user

                var body: String

                if(listMov.size == 1) {
                    var mov = listMov[0]
                    body = "Amanhã vence a despesa:\n->${mov.descriptionMovement}, no valor de: R$ ${mov.valueMovement}"
                } else {
                    body = "Amanhã vencem as seguintes despesas: \n"
                    for (mov in listMov) {
                        body += "-> ${mov.descriptionMovement}, no valor de: R$ ${mov.valueMovement} \n"
                    }
                }

                val toEMail: String? = usuario?.email
                val subject: String = "Aviso de Vencimento"

                emailService.sendEmail(toEMail!!, subject, body)
            }
        }
    }

    fun overdueAccount() {
        var dto: MovementDTO = MovementDTO().apply {
            wasPaid = false
            typeMovement = TypeMovement.DESPESA.toString()
            var dataFilter = LocalDate.now().minusDays(1)
            dueDateIni = dataFilter
            dueDateEnd = dataFilter
        }

        var filterMovement = FilterMovementSpecification(dto)
        var listMovements = repository.findAll(filterMovement)

        val mapUser: Map<Long?, List<Movement>> = listMovements.groupBy { it.user?.id }

        if(mapUser.isNotEmpty()) {
            for (listMov in mapUser.values) {
                val usuario = listMov[0].user

                var body: String

                if(listMovements.size == 1) {
                    var mov = listMovements[0]
                    body = "Informativo:\n\nNão ocorreu o registro de pagamento da despesa:\n->${mov.descriptionMovement}, no valor de: R$ ${mov.valueMovement}"
                } else {
                    body = "Informativo:\n\nNão ocorreu o registro de pagamento das seguintes despesas: \n"
                    for (mov in listMovements) {
                        body += "-> ${mov.descriptionMovement}, no valor de: R$ ${mov.valueMovement} \n"
                    }
                }

                val toEMail: String? = usuario?.email
                val subject: String = "Atenção! Você possui despesa(s) vencida(s)"

                emailService.sendEmail(toEMail!!, subject, body)
            }
        }
    }

}