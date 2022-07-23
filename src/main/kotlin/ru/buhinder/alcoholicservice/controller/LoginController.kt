package ru.buhinder.alcoholicservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/login")
class LoginController {

    @GetMapping
    fun login(): Mono<Void> {
        return Mono.empty()
    }

}
