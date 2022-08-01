package ru.buhinder.alcoholicservice.service

import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.repository.AlcoholicDaoFacade

@Service
class AlcoholicService(
    private val alcoholicDaoFacade: AlcoholicDaoFacade,
    private val conversionService: ConversionService,
) {

    fun get(email: String): Mono<AlcoholicResponse> {
        return alcoholicDaoFacade.getByEmail(email)
            .map { conversionService.convert(it, AlcoholicResponse::class.java)!! }
    }

}
