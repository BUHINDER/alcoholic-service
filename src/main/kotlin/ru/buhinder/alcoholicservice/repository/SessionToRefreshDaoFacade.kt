package ru.buhinder.alcoholicservice.repository

import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.CriteriaDefinition
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.config.LoggerDelegate
import ru.buhinder.alcoholicservice.entity.SessionToRefreshEntity
import java.util.UUID

@Repository
class SessionToRefreshDaoFacade(
    private val r2dbcEntityOperations: R2dbcEntityOperations,
) {
    private val logger by LoggerDelegate()

    fun insert(sessionToRefreshEntity: SessionToRefreshEntity): Mono<SessionToRefreshEntity> {
        return Mono.just(logger.info("Trying to save SessionToRefreshEntity $sessionToRefreshEntity"))
            .flatMap { r2dbcEntityOperations.insert(sessionToRefreshEntity) }
            .doOnSuccess { logger.info("Saved SessionToRefreshEntity with id ${it.id}") }
            .doOnError { logger.error("Error saving SessionToRefreshEntity $sessionToRefreshEntity") }
    }

    fun invalidateRefreshToken(sessionId: UUID): Mono<UUID> {
        return Mono.just(logger.info("Trying to invalidate SessionToRefreshEntity with session id $sessionId"))
            .flatMap {
                r2dbcEntityOperations.update(
                    Query.query(
                        CriteriaDefinition.from(
                            Criteria.where("session_id").`is`(sessionId),
                            Criteria.where("is_active").`is`(true),
                        )
                    ),
                    Update.update("is_active", false),
                    SessionToRefreshEntity::class.java
                )
            }
            .doOnSuccess { logger.info("Invalidated $it rows for SessionToRefreshEntity with session id $sessionId") }
            .doOnError { logger.error("Error invalidating SessionToRefreshEntity with session id $sessionId") }
            .map { sessionId }
    }

}
