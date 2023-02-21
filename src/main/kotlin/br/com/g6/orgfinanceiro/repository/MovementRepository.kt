package br.com.g6.orgfinanceiro.repository

import br.com.g6.orgfinanceiro.model.Movement
import br.com.g6.orgfinanceiro.model.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface MovementRepository : JpaRepository<Movement, Long>, JpaSpecificationExecutor<Movement> {

}