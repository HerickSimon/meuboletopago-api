package br.com.g6.orgfinanceiro.services

import br.com.g6.orgfinanceiro.model.Users
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(val user: Users?) : UserDetails{
//    @Serial
    private val serialVersionUID = 1L;
    private var password: String = user!!.password
    private var username: String = user!!.email

    //nome e senha
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null;
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}