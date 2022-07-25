package ru.buhinder.alcoholicservice.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.repository.SessionDaoFacade
import ru.buhinder.alcoholicservice.repository.SessionToRefreshDaoFacade
import ru.buhinder.alcoholicservice.service.validation.SessionValidationService

@Service
class LogoutService(
    private val tokenService: TokenService,
    private val sessionDaoFacade: SessionDaoFacade,
    private val sessionToRefreshDaoFacade: SessionToRefreshDaoFacade,
    private val sessionValidationService: SessionValidationService,
) {

    fun logout(jwt: String): Mono<Int> {
        return tokenService.validateToken(jwt)
            .flatMap { tokenService.getSession(it) }
            .flatMap { sessionValidationService.validateSessionIsActive(it) }
            .flatMap { sessionToRefreshDaoFacade.invalidateRefreshToken(it) }
            .flatMap { sessionDaoFacade.invalidateSession(it) }
    }

}
