package ru.buhinder.alcoholicservice.controller

import java.security.Principal
import java.util.UUID
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.dto.response.IdResponse
import ru.buhinder.alcoholicservice.service.AlcoholicService
import ru.buhinder.alcoholicservice.service.ImageService

@RestController
@RequestMapping("/api/alcoholic/image")
class ImageController(
    private val imageService: ImageService,
    private val alcoholicService: AlcoholicService,
) {

    @GetMapping("/{imageId}", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImage(@PathVariable imageId: UUID): Flux<DataBuffer> {
        return imageService.getImage(imageId)
    }

    @PostMapping
    fun addImage(
        principal: Principal,
        @RequestBody image: Mono<FilePart>,
    ): Mono<IdResponse> {
        return image
            .flatMap { alcoholicService.addNewImage(UUID.fromString(principal.name), it) }
            .map { IdResponse(it) }
    }

}