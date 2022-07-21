package ru.buhinder.alcoholicservice.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.config.LoggerDelegate
import ru.buhinder.alcoholicservice.dto.AlcoholicDto

@Service
class PasswordService(
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger by LoggerDelegate()

    fun encodePassword(dto: Mono<AlcoholicDto>): Mono<String> {
        return dto.map { it.password }
            .map { passwordEncoder.encode(it) }
            .doOnNext { logger.info("Encoded alcoholic's password") }
    }

}
