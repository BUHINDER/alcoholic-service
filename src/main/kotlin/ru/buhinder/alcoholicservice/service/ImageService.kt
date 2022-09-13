package ru.buhinder.alcoholicservice.service

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import java.io.SequenceInputStream
import java.util.UUID
import org.springframework.http.MediaType.IMAGE_JPEG_VALUE
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono
import ru.buhinder.alcoholicservice.config.properties.MinioProperties
import ru.buhinder.alcoholicservice.util.toUUID


@Service
class ImageService(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties,
) {

    fun saveAlcoholicImage(image: FilePart): Mono<UUID> {
        val imageId = UUID.randomUUID()
        return image.toMono()
            .publishOn(Schedulers.boundedElastic())
            .flatMapMany { it.content() }
            .map { it.asInputStream(true) }
            .reduce(::SequenceInputStream)
            .map {
                PutObjectArgs.builder()
                    .`object`("$imageId")
                    .bucket(minioProperties.bucket)
                    .contentType(IMAGE_JPEG_VALUE)
                    .userMetadata(mapOf("originalName" to image.filename()))
                    .stream(it, -1, 6000000L)
                    .build()
            }
            .map {
                minioClient.putObject(it).`object`().toUUID()
            }
    }

    fun getImage(imageId: UUID): Mono<ByteArray> {
        return Mono.just(imageId)
            .map {
                minioClient.getObject(
                    GetObjectArgs.builder()
                        .bucket(minioProperties.bucket)
                        .`object`("$it")
                        .build()
                )
            }
            .publishOn(Schedulers.boundedElastic())
            .map { it.readAllBytes() }
    }

}
