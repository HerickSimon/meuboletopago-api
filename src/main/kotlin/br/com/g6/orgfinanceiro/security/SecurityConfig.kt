package br.com.g6.orgfinanceiro.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig() {

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }


    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf().disable()
            .cors()
        http.httpBasic().and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/user/login").permitAll()
            .antMatchers(HttpMethod.POST, "/user/signup").permitAll()
            .antMatchers(HttpMethod.POST, "/user/forgot-password").permitAll()
            .antMatchers(HttpMethod.POST, "/user/password-recovery").permitAll()
            .antMatchers(HttpMethod.GET, "/user/users").authenticated()
            .antMatchers(HttpMethod.GET, "/user/{userId}").authenticated()
            .antMatchers(HttpMethod.PUT, "/user/{userId}").authenticated()
            .antMatchers(HttpMethod.DELETE, "/user/{userId}").authenticated()
            .antMatchers(HttpMethod.GET, "/movement/**").authenticated()
            .antMatchers(HttpMethod.POST, "/movement/**").authenticated()
            .antMatchers(HttpMethod.PUT, "/movement/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/movement/**").authenticated()
//            .anyRequest().authenticated()
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)


        return http.build()
    }

}

//    @Bean
//    fun userDetailsService(): UserDetailsService {
//        val userDetails = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build()
//        return InMemoryUserDetailsManager(userDetails)
//    }
