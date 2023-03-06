package br.com.g6.orgfinanceiro.services

import br.com.g6.orgfinanceiro.dto.MovementDTO
import br.com.g6.orgfinanceiro.model.Movement
import br.com.g6.orgfinanceiro.model.Users
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class FilterMovementSpecification(var dto : MovementDTO) : Specification<Movement> {
    override fun toPredicate(root: Root<Movement>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val predicates: MutableList<Predicate> = mutableListOf()

        if(dto == null) return builder.and()

        if(dto.idMovement != null)
            predicates.add(builder.equal(root.get<Long>("idMovement"), dto.idMovement))

        if(dto.idUser != null) {
            root.fetches
            predicates.add(builder.equal(root.get<Users>("user").get<Long>("id"), dto.idUser));
        }

        if(dto.descriptionMovement != null)
            predicates.add(builder.like(root.get<String>("descriptionMovement"), "%"+dto.descriptionMovement+"%"))

        if(dto.typeMovement != null)
            predicates.add(builder.equal(root.get<Int>("typeMovement"), dto.typeMovement))

        if(dto.wasPaid != null)
            predicates.add(builder.equal(root.get<Boolean>("wasPaid"), dto.wasPaid))

        if(dto.dueDateIni != null && dto.dueDateEnd == null) {
            predicates.add(builder.greaterThan(root.get("dueDate"), dto.dueDateIni!!));
        } else if(dto.dueDateEnd != null && dto.dueDateIni == null) {
            predicates.add(builder.lessThan(root.get("dueDate"), dto.dueDateEnd!!));
        } else if(dto.dueDateIni != null && dto.dueDateEnd != null) {
            predicates.add(builder.between(root.get("dueDate"), dto.dueDateIni!!, dto.dueDateEnd!!));
        }

        if(dto.valueMovementIni != null && dto.valueMovementEnd == null) {
            predicates.add(builder.greaterThan(root.get("valueMovement"), dto.valueMovementIni!!));
        } else if(dto.valueMovementEnd != null && dto.valueMovementIni == null) {
            predicates.add(builder.lessThan(root.get("valueMovement"), dto.valueMovementEnd!!));
        } else if(dto.valueMovementIni != null && dto.valueMovementEnd != null) {
            predicates.add(builder.between(root.get("valueMovement"), dto.valueMovementIni!!, dto.valueMovementEnd!!));
        }

        return builder.and(*predicates.toTypedArray())
    }

}