package ru.buhinder.alcoholicservice.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.enums.Role
import ru.buhinder.alcoholicservice.enums.Role.ROLE_DRINKER
import java.time.Duration
import java.time.Instant
import java.util.Date

@Service
class TokenService(
    private val algorithm: Algorithm,
    private val jwtVerifier: JWTVerifier
) {

    fun createToken(subject: String): String {
        return JWT.create()
            .withSubject(subject)
            //TODO replace with properties with conversion to date
            .withExpiresAt(Date.from(Instant.now().plus(Duration.ofMinutes(60))))
            .withClaim("roles", listOf("$ROLE_DRINKER"))
            .sign(algorithm)
    }

    fun buildAuthorities(roles: Array<Role>): List<SimpleGrantedAuthority> {
        return roles
            .map { SimpleGrantedAuthority(it.name) }
            .toList()
    }

    fun validateToken(token: String): Mono<DecodedJWT> {
        return jwtVerifier.verify(token)
            .toMono()
    }

}
