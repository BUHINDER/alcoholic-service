package ru.buhinder.alcoholicservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.service.AlcoholicService
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/api/alcoholic/alcoholic")
class AlcoholicController(
    private val alcoholicService: AlcoholicService,
) {

    @GetMapping("/{email}")
    fun get(@PathVariable email: String): Mono<AlcoholicResponse> {
        return alcoholicService.get(email)
    }

    @GetMapping("/own")
    fun getOwnInfo(principal: Principal): Mono<AlcoholicResponse> {
        return alcoholicService.get(UUID.fromString(principal.name))
    }

}
