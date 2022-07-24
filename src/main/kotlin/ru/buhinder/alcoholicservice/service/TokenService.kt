package ru.buhinder.alcoholicservice.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.http.ResponseCookie
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.config.LoggerDelegate
import ru.buhinder.alcoholicservice.dto.AccessTokenDto
import ru.buhinder.alcoholicservice.dto.response.AuthResponse
import ru.buhinder.alcoholicservice.entity.SessionToRefreshEntity
import ru.buhinder.alcoholicservice.enums.Role
import ru.buhinder.alcoholicservice.enums.Role.ROLE_DRINKER
import ru.buhinder.alcoholicservice.repository.SessionToRefreshDaoFacade
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.UUID

@Service
class TokenService(
    private val algorithm: Algorithm,
    private val jwtVerifier: JWTVerifier,
    private val sessionToRefreshDaoFacade: SessionToRefreshDaoFacade,
) {
    private val logger by LoggerDelegate()

    companion object {
        val refreshTokenDuration: Duration = Duration.ofDays(1)
    }

    fun refreshToken(refreshToken: String): Mono<AuthResponse> {
        return validateToken(refreshToken)
            .flatMap { jwt ->
                getSession(jwt)
                    .map { sessionToRefreshDaoFacade.invalidateRefreshToken(it) }
                    .flatMap { getSubject(jwt).zipWith(it) }
                    .flatMap { alcIdToSesId ->
                        val newAccessToken = createAccessToken(alcIdToSesId.t1)
                        val newRefreshToken = createRefreshToken(alcIdToSesId.t1, alcIdToSesId.t2)
                        val s2re = SessionToRefreshEntity(UUID.randomUUID(), alcIdToSesId.t2)
                        val newRefreshResponseCookie = createRefreshTokenCookie(newRefreshToken)
                        sessionToRefreshDaoFacade.insert(s2re)
                            .map {
                                AuthResponse(
                                    AccessTokenDto(newAccessToken),
                                    newRefreshResponseCookie
                                )
                            }
                    }
            }
    }

    fun createAccessToken(subject: UUID): String {
        return JWT.create()
            .withSubject("$subject")
            //TODO replace with properties with conversion to date
            .withExpiresAt(Date.from(Instant.now().plus(Duration.ofMinutes(60))))
            .withClaim("roles", listOf("$ROLE_DRINKER"))
            .sign(algorithm)
    }

    fun createRefreshToken(subject: UUID, sessionUUID: UUID): String {
        return JWT.create()
            .withSubject("$subject")
            //TODO replace with properties with conversion to date
            .withExpiresAt(Date.from(Instant.now().plus(refreshTokenDuration)))
            .withClaim("session", "$sessionUUID")
            .sign(algorithm)
    }

    fun getSubject(jwt: DecodedJWT): Mono<UUID> {
        return Mono.just(jwt.subject)
            .map { UUID.fromString(it) }

    }

    fun createRefreshTokenCookie(refreshToken: String): ResponseCookie {
        return ResponseCookie.fromClientResponse("refreshToken", refreshToken)
            .httpOnly(true)
            .path("/api/refresh")
            .sameSite("Strict")
            .maxAge(refreshTokenDuration)
            .build()
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

    fun getClaim(jwt: DecodedJWT, claim: String): Mono<Claim> {
        return Mono.justOrEmpty(jwt.claims["session"])
    }

    fun getSession(jwt: DecodedJWT): Mono<UUID> {
        return getClaim(jwt, "session")
            .map { UUID.fromString("$it".replace("\"", "")) }
            .doOnError { logger.error("Error getting session clam") }
            .onErrorResume { Mono.empty() }
    }

}
