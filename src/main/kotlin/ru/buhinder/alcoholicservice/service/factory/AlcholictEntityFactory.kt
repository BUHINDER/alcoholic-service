package ru.buhinder.alcoholicservice.service.factory

import java.util.UUID
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.entity.AlcoholicEntity

fun createAlcoholicEntity(
    alcoholicDto: AlcoholicDto,
    encodedPassword: String,
    photoId: UUID?,
) = AlcoholicEntity(
    id = UUID.randomUUID(),
    firstname = alcoholicDto.firstname,
    lastName = alcoholicDto.lastName,
    age = alcoholicDto.age,
    login = alcoholicDto.login,
    password = encodedPassword,
    email = alcoholicDto.email,
    photoId = photoId
)