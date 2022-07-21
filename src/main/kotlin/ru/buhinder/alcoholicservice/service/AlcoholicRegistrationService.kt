package ru.buhinder.alcoholicservice.service

import org.springframework.core.convert.ConversionService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.config.LoggerDelegate
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.entity.AlcoholicEntity
import ru.buhinder.alcoholicservice.repository.AlcoholicDaoFacade
import ru.buhinder.alcoholicservice.service.validation.RegistrationValidationService
import java.util.UUID

@Service
class AlcoholicRegistrationService(
    private val alcoholicDaoFacade: AlcoholicDaoFacade,
    private val conversionService: ConversionService,
    private val passwordEncoder: PasswordEncoder,
    private val registrationValidationService: RegistrationValidationService,
) {
    private val logger by LoggerDelegate()

    fun register(dto: AlcoholicDto): Mono<AlcoholicResponse> {
        return dto.toMono()
            .doOnNext { logger.info("Registering user") }
            .map { registrationValidationService.validateLoginDoesNotExist(it) }
            .flatMap {
                it.zipWith(encodePassword(it))
                { dto, password ->
                    AlcoholicEntity(
                        id = UUID.randomUUID(),
                        firstname = dto.firstname,
                        lastName = dto.lastName,
                        age = dto.age,
                        login = dto.login,
                        password = password
                    )
                }
                    .flatMap { entity -> alcoholicDaoFacade.insert(entity) }
            }
            .map { conversionService.convert(it, AlcoholicResponse::class.java)!! }
    }

    private fun encodePassword(dto: Mono<AlcoholicDto>): Mono<String> {
        return dto.map { it.password }
            .map { passwordEncoder.encode(it) }
            .doOnNext { logger.info("Encoded user's password") }
    }

}
