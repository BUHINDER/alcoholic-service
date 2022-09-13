package ru.buhinder.alcoholicservice.model

import java.util.UUID

data class JwtContextModel(
    val displayName: String,
    val photoId: UUID? = UUID.fromString("00000000-0000-0000-0000-000000000000"),
)
