package ru.buhinder.alcoholicservice.service.validation

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.controller.advice.exception.EntityAlreadyExistsException
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.repository.AlcoholicDaoFacade

@Service
class RegistrationValidationService(
    private val alcoholicDaoFacade: AlcoholicDaoFacade,
) {

    fun validateLoginDoesNotExist(dto: AlcoholicDto): Mono<AlcoholicDto> {

        return alcoholicDaoFacade.existsByLogin(dto.login)
            .filter { it }
            .flatMap<AlcoholicDto> {
                Mono.error(
                    EntityAlreadyExistsException(
                        message = "Alcoholic with login ${dto.login} already exists",
                        payload = emptyMap()
                    )
                )
            }
            .switchIfEmpty { dto.toMono() }
    }


    fun validateEmailDoesNotExists(dto: AlcoholicDto): Mono<AlcoholicDto> {

        return alcoholicDaoFacade.existsByEmail(dto.email)
            .filter { it }
            .flatMap<AlcoholicDto> {
                Mono.error(
                    EntityAlreadyExistsException(
                        message = "Alcoholic with email ${dto.email} already exists",
                        payload = emptyMap()
                    )
                )
            }
            .switchIfEmpty { dto.toMono() }
    }
}