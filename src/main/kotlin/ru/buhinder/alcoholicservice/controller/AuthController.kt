package ru.buhinder.alcoholicservice.controller

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.service.LogoutService
import ru.buhinder.alcoholicservice.service.RegistrationService
import ru.buhinder.alcoholicservice.service.TokenService
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class AuthController(
    private val registrationService: RegistrationService,
    private val tokenService: TokenService,
    private val logoutService: LogoutService,
) {

    @PostMapping("/login")
    fun login(): Mono<ResponseEntity<Void>> {
        return ResponseEntity.ok()
            .build<Void>()
            .toMono()
    }

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(value = "refreshToken") refreshToken: String
    ): Mono<ResponseEntity<Void>> {
        return tokenService.refreshToken(refreshToken)
            .map {
                ResponseEntity.ok()
                    .header(AUTHORIZATION, it.accessToken.accessToken)
                    .header(SET_COOKIE, "${it.responseCookie}")
                    .build()
            }
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader(AUTHORIZATION) authorizationHeader: String): Mono<ResponseEntity<Void>> {
        return logoutService.logout(authorizationHeader)
            .map { ResponseEntity.ok().build() }
    }

    @PostMapping("/register")
    fun registerAlcoholic(
        @Valid
        @RequestBody dto: AlcoholicDto
    ): Mono<AlcoholicResponse> {
        return registrationService.register(dto)
    }

}
