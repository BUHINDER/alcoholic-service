package ru.buhinder.alcoholicservice.service.validation

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.repository.SessionDaoFacade
import java.util.UUID

@Service
class SessionValidationService(
    private val sessionDaoFacade: SessionDaoFacade
) {

    fun validateSessionIsActive(sessionId: UUID): Mono<UUID> {
        return sessionDaoFacade.getByIdAndIsActiveIsTrue(sessionId)
            .map { it.id }
    }

}
