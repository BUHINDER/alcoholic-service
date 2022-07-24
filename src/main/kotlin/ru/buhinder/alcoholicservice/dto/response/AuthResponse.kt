package ru.buhinder.alcoholicservice.dto.response

import org.springframework.http.ResponseCookie
import ru.buhinder.alcoholicservice.dto.AccessTokenDto

data class AuthResponse(
    val accessToken: AccessTokenDto,
    val responseCookie: ResponseCookie
)
