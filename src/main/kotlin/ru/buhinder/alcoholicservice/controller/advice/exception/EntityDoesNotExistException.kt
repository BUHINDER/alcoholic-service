package ru.buhinder.alcoholicservice.controller.advice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import ru.buhinder.alcoholicservice.controller.advice.dto.AlcoholicErrorCode.DOES_NOT_EXIST
import ru.buhinder.alcoholicservice.controller.advice.dto.ErrorCode

class EntityDoesNotExistException(
    responseStatus: HttpStatus = NOT_FOUND,
    code: ErrorCode = DOES_NOT_EXIST,
    message: String,
    payload: Map<String, Any>,
) : AlcoholicApiException(
    responseStatus = responseStatus,
    code = code,
    message = message,
    payload = payload
)
