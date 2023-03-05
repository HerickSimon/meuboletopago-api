package br.com.g6.orgfinanceiro.model

 class UsersLogin(
     var id: Long,
     var email: String,
     var password: String,
     var token: String = "token"
 )
