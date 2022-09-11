package ru.buhinder.alcoholicservice.service

import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.repository.AlcoholicDaoFacade
import java.util.UUID

@Service
class AlcoholicService(
    private val alcoholicDaoFacade: AlcoholicDaoFacade,
    private val conversionService: ConversionService,
) {

    fun get(email: String): Mono<AlcoholicResponse> {
        return alcoholicDaoFacade.getByEmail(email)
            .map { conversionService.convert(it, AlcoholicResponse::class.java)!! }
    }

    fun get(alcoholicId: UUID): Mono<AlcoholicResponse> {
        return alcoholicDaoFacade.getById(alcoholicId)
            .map { conversionService.convert(it, AlcoholicResponse::class.java)!! }
    }

    fun getByIds(alcoholicIds: List<UUID>): Flux<AlcoholicResponse> {
        return alcoholicDaoFacade.findByIds(alcoholicIds)
            .map { conversionService.convert(it, AlcoholicResponse::class.java)!! }
    }
}
