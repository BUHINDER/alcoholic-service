package ru.buhinder.alcoholicservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
class AlcoholicServiceApplication

fun main(args: Array<String>) {
    runApplication<AlcoholicServiceApplication>(*args)
}
