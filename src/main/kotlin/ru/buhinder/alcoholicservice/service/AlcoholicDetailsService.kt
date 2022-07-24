package ru.buhinder.alcoholicservice.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.controller.advice.exception.EntityDoesNotExistException
import ru.buhinder.alcoholicservice.enums.Role.ROLE_DRINKER
import ru.buhinder.alcoholicservice.repository.AlcoholicDaoFacade

@Service
class AlcoholicDetailsService(
    private val daoFacade: AlcoholicDaoFacade
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return daoFacade.findByLogin(username)
            .switchIfEmpty {
                EntityDoesNotExistException(
                    message = "No alcoholic found with username $username",
                    payload = mapOf("login" to username)
                ).toMono()
            }
            .map {
                User(
                    "${it.id}",
                    "{bcrypt}${it.password}",
                    listOf(SimpleGrantedAuthority("$ROLE_DRINKER"))
                )
            }
    }

}
