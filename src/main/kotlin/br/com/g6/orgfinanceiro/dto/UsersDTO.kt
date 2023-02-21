package br.com.g6.orgfinanceiro.dto

import org.jetbrains.annotations.NotNull
import javax.validation.constraints.Email

data class UsersDTO(
    @NotNull
    var name: String,
    @NotNull
    @Email
    var email: String,
    @NotNull
    var password: String)
