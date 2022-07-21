package ru.buhinder.alcoholicservice.controller.advice.dto

enum class AlcoholicErrorCode : ErrorCode {

    UNKNOWN_EXCEPTION,
    VALIDATION_ERROR,
    CANNOT_BE_CREATED,
    CANNOT_BE_UPDATED,
    CANNOT_BE_DEACTIVATED,
    DOES_NOT_EXIST,
    ALREADY_EXISTS,

}
