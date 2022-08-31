package ru.buhinder.alcoholicservice.entity

import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("alcoholic_photo")
open class AlcoholicPhotoEntity(
    id: UUID? = null,
    val eventId: UUID,
    val photoId: UUID,
    private val createdAt: Long? = Instant.now().toEpochMilli(),
)
