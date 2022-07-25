package ru.buhinder.alcoholicservice.service

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.entity.SessionEntity
import ru.buhinder.alcoholicservice.entity.SessionToRefreshEntity
import ru.buhinder.alcoholicservice.repository.SessionDaoFacade
import ru.buhinder.alcoholicservice.repository.SessionToRefreshDaoFacade
import java.util.UUID

@Component
class BasicAuthenticationSuccessHandler(
    private val tokenService: TokenService,
    private val sessionDaoFacade: SessionDaoFacade,
    private val sessionToRefreshDaoFacade: SessionToRefreshDaoFacade,
) : ServerAuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication
    ): Mono<Void> {
        return Mono.just(webFilterExchange.exchange)
            .flatMap { ex ->
                val sessionId = UUID.randomUUID()
                val alcoholicId = UUID.fromString(authentication.name)
                val accessToken = tokenService.createAccessToken(alcoholicId, sessionId)
                val refreshToken = tokenService.createRefreshToken(alcoholicId, sessionId)
                val refreshTokenCookie = tokenService.createRefreshTokenCookie(refreshToken)

                ex.response.headers.add(AUTHORIZATION, accessToken)
                ex.response.cookies.add(SET_COOKIE, refreshTokenCookie)

                val session = SessionEntity(id = sessionId, alcoholicId = alcoholicId)
                val sessionToRefresh = SessionToRefreshEntity(UUID.randomUUID(), sessionId)

                sessionDaoFacade.insert(session)
                    .flatMap { sessionToRefreshDaoFacade.insert(sessionToRefresh) }
                    .map { ex }
            }
            .flatMap { webFilterExchange.chain.filter(it) }
    }

}
