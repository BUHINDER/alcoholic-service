package ru.buhinder.alcoholicservice.util

import java.util.UUID

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}