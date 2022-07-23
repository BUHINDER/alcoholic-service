package ru.buhinder.alcoholicservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.service.RegistrationService
import javax.validation.Valid

@RestController
@RequestMapping("/api/alcoholic")
class AuthenticationController(
    private val registrationService: RegistrationService,
) {

    @PostMapping("/logout")
    fun logout(): Mono<String> {
        return "Drink"
            .toMono()
    }

    @GetMapping("/refresh")
    fun refresh(): Mono<String> {
        return "Refresh"
            .toMono()
    }

    @PostMapping("/register")
    fun registerAlcoholic(@Valid @RequestBody dto: AlcoholicDto): Mono<AlcoholicResponse> {
        return registrationService.register(dto)
    }

}
