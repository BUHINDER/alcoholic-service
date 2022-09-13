package ru.buhinder.alcoholicservice.controller

import java.util.UUID
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.buhinder.alcoholicservice.service.ImageService

@RestController
@RequestMapping("/api/alcoholic/image")
class ImageController(
    private val imageService: ImageService,
) {

    @GetMapping("/{imageId}", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImage(@PathVariable imageId: UUID): Mono<ByteArray> {
        return imageService.getImage(imageId)
    }

}