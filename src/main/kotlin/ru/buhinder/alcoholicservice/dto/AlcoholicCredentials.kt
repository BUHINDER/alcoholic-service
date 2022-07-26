package ru.buhinder.alcoholicservice.dto

import javax.validation.constraints.NotBlank

data class AlcoholicCredentials(

    @field:NotBlank(message = "login is required")
    val login: String,
    @field:NotBlank(message = "password is required")
    val password: String,

    )
