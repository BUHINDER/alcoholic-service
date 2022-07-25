package ru.buhinder.alcoholicservice.controller.advice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import ru.buhinder.alcoholicservice.controller.advice.dto.AlcoholicErrorCode
import ru.buhinder.alcoholicservice.controller.advice.dto.ErrorCode

class EntityNotFoundException(
    responseStatus: HttpStatus = NOT_FOUND,
    code: ErrorCode = AlcoholicErrorCode.NOT_FOUND,
    message: String,
    payload: Map<String, Any>,
) : AlcoholicApiException(
    responseStatus = responseStatus,
    code = code,
    message = message,
    payload = payload
)
