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
        return dto.toMono()
            .flatMap { alcoholicDaoFacade.findByLogin(it.login) }
            .map {
                throw EntityAlreadyExistsException(
                    message = "Alcoholic already exists",
                    payload = emptyMap()
                )
                dto
            }
            .switchIfEmpty { dto.toMono() }
    }

}
