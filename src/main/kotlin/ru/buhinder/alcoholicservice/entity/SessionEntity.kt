package ru.buhinder.alcoholicservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("session")
data class SessionEntity(
    @Id val id: UUID,
    val alcoholicId: UUID,
    val createdAt: Long? = Instant.now().toEpochMilli(),
    val isActive: Boolean? = true,
)
