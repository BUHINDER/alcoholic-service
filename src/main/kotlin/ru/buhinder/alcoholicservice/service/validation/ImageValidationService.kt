package ru.buhinder.alcoholicservice.service.validation

import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.controller.advice.exception.EntityCannotBeCreatedException

@Service
class ImageValidationService {

    fun validateImageFormat(image: FilePart): Mono<FilePart> {

        return image.toMono()
            .filter {
                val contentType = it.headers().contentType
                contentType != null && contentType == MediaType.IMAGE_JPEG
            }
            .switchIfEmpty(
                Mono.error(
                    EntityCannotBeCreatedException(
                        message = "Invalid media type. Only .jpeg files are allowed",
                        payload = emptyMap()
                    )
                )
            )
    }
}