package ru.buhinder.alcoholicservice.repository

import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.CriteriaDefinition
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.config.LoggerDelegate
import ru.buhinder.alcoholicservice.entity.AlcoholicEntity

@Repository
class AlcoholicDaoFacade(
    private val r2dbcEntityOperations: R2dbcEntityOperations,
) {
    private val logger by LoggerDelegate()

    fun insert(entity: AlcoholicEntity): Mono<AlcoholicEntity> {
        return Mono.just(logger.info("Trying to save AlcoholicEntity with id ${entity.id}"))
            .flatMap { r2dbcEntityOperations.insert(entity) }
            .doOnNext { logger.info("Saved AlcoholicEntity with id ${entity.id}") }
            .doOnError { logger.info("Error saving AlcoholicEntity with id ${entity.id}") }

    }

    fun findByLogin(login: String): Mono<AlcoholicEntity> {
        return Mono.just(logger.info("Trying to find AlcoholicEntity with login $login"))
            .flatMap {
                r2dbcEntityOperations.selectOne(
                    Query.query(CriteriaDefinition.from(Criteria.where("login").`is`(login))),
                    AlcoholicEntity::class.java
                )
            }
            .doOnNext { logger.info("Found AlcoholicEntity with login $login") }
            .doOnError { logger.info("Error retrieving AlcoholicEntity with login $login") }
    }

}