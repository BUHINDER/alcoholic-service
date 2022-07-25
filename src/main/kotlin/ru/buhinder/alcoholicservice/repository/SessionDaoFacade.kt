package ru.buhinder.alcoholicservice.repository

import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.CriteriaDefinition
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.buhinder.alcoholicservice.config.LoggerDelegate
import ru.buhinder.alcoholicservice.controller.advice.exception.EntityNotFoundException
import ru.buhinder.alcoholicservice.entity.SessionEntity
import java.util.UUID

@Repository
class SessionDaoFacade(
    private val r2dbcEntityOperations: R2dbcEntityOperations,
) {
    private val logger by LoggerDelegate()

    fun insert(session: SessionEntity): Mono<SessionEntity> {
        return Mono.just(logger.info("Trying to save SessionEntity $session"))
            .flatMap { r2dbcEntityOperations.insert(session) }
            .doOnSuccess { logger.info("Saved SessionEntity with id ${it.id}") }
            .doOnError { logger.error("Error saving SessionEntity $session") }

    }

    fun getByIdAndIsActiveIsTrue(id: UUID): Mono<SessionEntity> {
        return r2dbcEntityOperations.selectOne(
            Query.query(
                CriteriaDefinition.from(
                    Criteria.where("id").`is`(id),
                    Criteria.where("is_active").isTrue
                )
            ),
            SessionEntity::class.java
        )
            .switchIfEmpty {
                Mono.error(
                    EntityNotFoundException(
                        message = "Session not found",
                        payload = mapOf("id" to id)
                    )
                )
            }
    }

    fun invalidateSession(sessionId: UUID): Mono<Int> {
        return Mono.just(logger.info("Trying to invalidate SessionEntity with id $sessionId"))
            .flatMap {
                r2dbcEntityOperations.update(
                    Query.query(
                        CriteriaDefinition.from(
                            Criteria.where("id").`is`(sessionId),
                            Criteria.where("is_active").isTrue
                        )
                    ),
                    Update.update("is_active", false),
                    SessionEntity::class.java
                )
            }
            .doOnSuccess { logger.info("Invalidated SessionEntity with id $sessionId") }
            .doOnError { logger.error("Error invalidating SessionEntity with id $sessionId") }
    }

}
