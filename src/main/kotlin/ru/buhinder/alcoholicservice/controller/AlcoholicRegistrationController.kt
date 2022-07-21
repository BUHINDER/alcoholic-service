package ru.buhinder.alcoholicservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.service.AlcoholicRegistrationService
import javax.validation.Valid

@RestController
@RequestMapping("/api/alcoholic")
class AlcoholicRegistrationController(
    private val alcoholicRegistrationService: AlcoholicRegistrationService,
) {

    @PostMapping("/register")
    fun registerAlcoholic(@Valid @RequestBody dto: AlcoholicDto): Mono<AlcoholicResponse> {
        return alcoholicRegistrationService.register(dto)
    }

}
