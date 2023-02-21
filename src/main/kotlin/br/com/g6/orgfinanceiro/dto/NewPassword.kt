package br.com.g6.orgfinanceiro.dto

import org.jetbrains.annotations.NotNull

data class NewPassword(
    @NotNull
    val token : String,
    @NotNull
    val password : String
)
