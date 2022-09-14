package ru.buhinder.alcoholicservice.service

import java.util.UUID
import org.springframework.core.convert.ConversionService
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.controller.advice.exception.EntityAlreadyExistsException
import ru.buhinder.alcoholicservice.dto.response.AlcoholicResponse
import ru.buhinder.alcoholicservice.repository.AlcoholicDaoFacade

@Service
class AlcoholicService(
    private val alcoholicDaoFacade: AlcoholicDaoFacade,
    private val conversionService: ConversionService,
    private val imageService: ImageService,
) {

    fun get(email: String): Mono<AlcoholicResponse> {
        return alcoholicDaoFacade.getByEmail(email)
            .map { conversionService.convert(it, AlcoholicResponse::class.java)!! }
    }

    fun get(alcoholicId: UUID): Mono<AlcoholicResponse> {
        return alcoholicDaoFacade.getById(alcoholicId)
            .map { conversionService.convert(it, AlcoholicResponse::class.java)!! }
    }

    fun addNewImage(id: UUID, image: FilePart): Mono<UUID> {
        return alcoholicDaoFacade.existsImageByAlcoholicId(id)
            .filter { it }
            .flatMap { imageService.saveAlcoholicImage(image) }
            .switchIfEmpty(
                Mono.error(
                    EntityAlreadyExistsException(
                        message = "Alcoholic already have person photo",
                        payload = emptyMap()
                    )
                )
            )
    }

}
