package ru.buhinder.alcoholicservice.dto

import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class AlcoholicDto(
    @field:NotBlank(message = "firstname is required")
    val firstname: String,

    @field:NotBlank(message = "lastName is required")
    val lastName: String,

    @field:Min(1, message = "age must be greater than 0")
    @field:Max(150, message = "age must be lower than 150")
    val age: Int?,

    @field:NotBlank(message = "login is required")
    val login: String,

    @field:NotBlank(message = "password is required")
    val password: String,

    @field:Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    @field:NotBlank(message = "email is required")
    val email: String,
)
