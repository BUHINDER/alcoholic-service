package ru.buhinder.alcoholicservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("alcoholic")
data class AlcoholicEntity(

    @Id val id: UUID,
    val firstname: String,
    val lastName: String,
    val age: Int?,
    val login: String,
    val password: String,

    )
