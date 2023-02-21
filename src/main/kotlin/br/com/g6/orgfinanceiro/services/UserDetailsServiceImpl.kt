package br.com.g6.orgfinanceiro.services

import br.com.g6.orgfinanceiro.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
       var user = userRepository.findByEmail(username)
        if(user != null){
            return  UserDetailsImpl(user);
        } else {
            throw  ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }
}