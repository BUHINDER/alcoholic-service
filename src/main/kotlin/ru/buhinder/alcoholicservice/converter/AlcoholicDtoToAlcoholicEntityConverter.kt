package ru.buhinder.alcoholicservice.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.buhinder.alcoholicservice.dto.AlcoholicDto
import ru.buhinder.alcoholicservice.entity.AlcoholicEntity
import java.util.UUID

@Component
class AlcoholicDtoToAlcoholicEntityConverter : Converter<AlcoholicDto, AlcoholicEntity> {

    override fun convert(source: AlcoholicDto): AlcoholicEntity {
        return AlcoholicEntity(
            id = UUID.randomUUID(),
            firstname = source.firstname,
            lastName = source.lastName,
            age = source.age,
            login = source.login,
            password = source.password,
        )
    }

}
