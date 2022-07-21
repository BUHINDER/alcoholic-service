package ru.buhinder.alcoholicservice.controller.advice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CONFLICT
import ru.buhinder.alcoholicservice.controller.advice.dto.AlcoholicErrorCode.ALREADY_EXISTS
import ru.buhinder.alcoholicservice.controller.advice.dto.ErrorCode

class EntityAlreadyExistsException(
    responseStatus: HttpStatus = CONFLICT,
    code: ErrorCode = ALREADY_EXISTS,
    message: String,
    payload: Map<String, Any>,
) : AlcoholicApiException(
    responseStatus = responseStatus,
    code = code,
    message = message,
    payload = payload
)
