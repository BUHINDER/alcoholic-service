package ru.buhinder.alcoholicservice.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.entity.AlcoholicEntity

@Component
class AlcoholicEntityToAlcoholicResponseConverter : Converter<AlcoholicEntity, AlcoholicResponse> {

    override fun convert(source: AlcoholicEntity): AlcoholicResponse {
        return AlcoholicResponse(
            id = source.id,
            firstname = source.firstname,
            lastName = source.lastName,
            age = source.age,
            email = source.email
        )
    }

}
