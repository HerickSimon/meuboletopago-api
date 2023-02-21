package br.com.g6.orgfinanceiro.services

import br.com.g6.orgfinanceiro.model.Users
import br.com.g6.orgfinanceiro.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class CurrentUserService {

    @Autowired
    private val userDetailsService: UserDetailsServiceImpl? = null

    @Autowired
    private lateinit var userRepository: UserRepository

    fun getCurrentUser(): Users? {
        val userDetails: UserDetails =
            SecurityContextHolder.getContext().authentication.principal as UserDetails
        return userRepository.findByEmail(userDetails.username)
    }

}