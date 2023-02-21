package br.com.g6.orgfinanceiro.repository

import br.com.g6.orgfinanceiro.model.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

//interface extends  JPAREPOSITORY -> CRUD de acesso ao banco de dados
@Repository
interface UserRepository : JpaRepository<Users, Long> {

    fun findByName(name: String?): Users?
    fun findByEmail(email: String?): Users?
    fun findById(userId:Long?): Users?
    fun findByRecoveryToken(token: String): Users?

}