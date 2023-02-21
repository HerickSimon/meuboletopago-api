package br.com.g6.orgfinanceiro.model

import br.com.g6.orgfinanceiro.enumeration.TypeMovement
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.jetbrains.annotations.NotNull
import org.springframework.data.jpa.repository.Temporal
import java.time.LocalDate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size


@Entity
@Table(name = "Movement")
 data class Movement (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idMovement: Long? = null,

    @NotNull
    var valueMovement: Double = 0.0,

    @NotNull
    @Temporal(TemporalType.DATE)
    var dueDate: LocalDate? = null,

    @NotNull
    @Size (min = 1, max = 500)
    var descriptionMovement: String? = null,

    var seqParcel: Int = 0,

    @NotNull
    var typeMovement: String? = null,

    @NotNull
    var wasPaid: Boolean? = null,

    @ManyToOne
    @NotNull
    var user: Users? = null
 )


