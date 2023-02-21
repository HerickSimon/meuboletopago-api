package br.com.g6.orgfinanceiro.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.util.*


data class MovementDTO (

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
    var dueDateIni: LocalDate? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
    var dueDateEnd: LocalDate? = null,

    var typeMovement: String? = null,
    var descriptionMovement: String? = null,
    var idUser: Long? = null,
    var valueMovementIni: Double? = null,
    var valueMovementEnd: Double? = null,
    var wasPaid: Boolean? = null,
    var idMovement: Long? = null
)

