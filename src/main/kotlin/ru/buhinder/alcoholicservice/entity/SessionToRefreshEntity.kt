package ru.buhinder.alcoholicservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("session_to_refresh")
data class SessionToRefreshEntity(
    @Id val id: UUID,
    val sessionId: UUID,
    val isActive: Boolean? = true,
)
