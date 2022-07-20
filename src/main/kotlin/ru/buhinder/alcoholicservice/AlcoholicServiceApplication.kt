package ru.buhinder.alcoholicservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AlcoholicServiceApplication

fun main(args: Array<String>) {
    runApplication<AlcoholicServiceApplication>(*args)
}
