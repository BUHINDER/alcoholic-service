package ru.buhinder.alcoholicservice.dto.response

import java.util.UUID

data class AlcoholicResponse(
    val id: UUID,
    val firstname: String,
    val lastName: String,
    val age: Int?,
    val email: String,
)
