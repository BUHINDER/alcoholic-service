package ru.buhinder.alcoholicservice.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class AlcoholicDto(

    val firstname: String,
    val lastName: String,
    @field:Min(1, message = "age must be greater than 0")
    @field:Max(150, message = "age must be lower than 150")
    val age: Int?,
    val login: String,
    val password: String,

    )
