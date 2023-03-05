package br.com.g6.orgfinanceiro.controller

import br.com.g6.orgfinanceiro.dto.BalanceDTO
import br.com.g6.orgfinanceiro.dto.MovementDTO
import br.com.g6.orgfinanceiro.model.Movement
import br.com.g6.orgfinanceiro.repository.MovementRepository
import br.com.g6.orgfinanceiro.services.CurrentUserService
import br.com.g6.orgfinanceiro.services.FilterMovementSpecification
import br.com.g6.orgfinanceiro.services.MovementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.validation.Valid

@RestController
@RequestMapping("/movement")
class MovementController {

    @Autowired
    lateinit var movementService: MovementService

    @Autowired
    lateinit var currentUserService: CurrentUserService

    @Autowired
    lateinit var repository: MovementRepository

    @PostMapping("/filter")
    fun findByFilter(@Valid @RequestBody dto: MovementDTO): MutableList<Movement> {
        dto.idUser = currentUserService.getCurrentUser()?.id
        var filterMovement = FilterMovementSpecification(dto)
        return repository.findAll(filterMovement)
    }
    @GetMapping()
    fun getAll(): MutableList<Movement> {
        return repository.findAll()
    }
    /*
    @GetMapping("/{idMovement}")
    fun getById(@PathVariable("idMovement") idMovement: Long,): Optional<Movement> {
        return repository.findById(idMovement)
    }

     */
    @GetMapping("/{idMovement}")
    fun getById(@PathVariable("idMovement") idMovement: Long): Any {
        val movement = repository.findById(idMovement)
        if (movement.isPresent){
            return ResponseEntity.ok(movement.get())
        } else {
            return ResponseEntity.notFound()
        }
    }


    @GetMapping("/balance")
    fun getBalance(): BalanceDTO {
        return movementService.getBalance()
    }

    @PostMapping()
    fun post(@Valid @RequestBody createMovement: Movement): Movement {
        createMovement.user = currentUserService.getCurrentUser()
        return repository.save(createMovement)
    }

    @PutMapping("/{idMovement}")
    fun put(@PathVariable("idMovement") idMovement: Long,@Valid @RequestBody newMovement: Movement):Movement {
        val updateMovement = repository.findById(idMovement).orElseThrow {EntityNotFoundException()}
        if(updateMovement.user != currentUserService.getCurrentUser())
            throw IllegalArgumentException("O lançamento não pertence a esse usuário")

        updateMovement?.apply {
            this.descriptionMovement = newMovement.descriptionMovement
            this.valueMovement = newMovement.valueMovement
            this.dueDate = newMovement.dueDate
            this.seqParcel = newMovement.seqParcel
            this.wasPaid = newMovement.wasPaid
        }

        return repository.save(updateMovement)
    }

    @DeleteMapping("/{idMovement}")
    fun delete(@PathVariable("idMovement") idMovement: Long){
        val deleteMovement = repository.findById(idMovement).orElseThrow {EntityNotFoundException()}
        if(deleteMovement.user != currentUserService.getCurrentUser())
            throw IllegalArgumentException("O lançamento não pertence a esse usuário")

        repository.delete(deleteMovement)
    }

    @DeleteMapping("/all")
    fun deleteAll() {
        return repository.deleteAll()
    }
}