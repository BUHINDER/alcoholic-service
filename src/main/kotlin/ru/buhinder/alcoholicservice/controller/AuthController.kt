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
import ru.buhinder.alcoholicservice.dto.AccessTokenDto
import ru.buhinder.alcoholicservice.dto.AlcoholicCredentials
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.service.AuthService
import ru.buhinder.alcoholicservice.service.TokenService
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class AuthController(
    private val authService: AuthService,
    private val tokenService: TokenService,
) {

    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody
        alcoholicCredentials: AlcoholicCredentials,
    ): Mono<ResponseEntity<AccessTokenDto>> {
        return authService.login(alcoholicCredentials)
            .map {
                ResponseEntity.ok()
                    .header(SET_COOKIE, "${it.responseCookie}")
                    .body(it.accessToken)
            }
    }

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(value = "refreshToken") refreshToken: String,
    ): Mono<ResponseEntity<AccessTokenDto>> {
        return tokenService.refreshToken(refreshToken)
            .map {
                ResponseEntity.ok()
                    .header(SET_COOKIE, "${it.responseCookie}")
                    .body(it.accessToken)
            }
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader(AUTHORIZATION) authorizationHeader: String): Mono<ResponseEntity<Void>> {
        return authService.logout(authorizationHeader)
            .map { ResponseEntity.ok().build() }
    }

    @PostMapping("/register")
    fun registerAlcoholic(
        @Valid
        @RequestBody dto: AlcoholicDto,
    ): Mono<AlcoholicResponse> {
        return authService.register(dto)
    }

}
