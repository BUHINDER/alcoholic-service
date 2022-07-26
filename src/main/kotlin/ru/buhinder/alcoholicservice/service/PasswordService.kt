package ru.buhinder.alcoholicservice.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.config.LoggerDelegate

@Service
class PasswordService(
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger by LoggerDelegate()

    fun comparePasswords(rawPassword: String, encodedPassword: String): Mono<Boolean> {
        return passwordEncoder.matches(rawPassword, encodedPassword).toMono()
            .doOnNext { logger.info("Passwords compared") }
    }

    fun encodePassword(password: String): Mono<String> {
        return password.toMono()
            .map { passwordEncoder.encode(it) }
            .doOnNext { logger.info("Encoded alcoholic's password") }
    }

}
