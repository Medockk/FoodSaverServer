package com.foodback.feature.users.impl.exception.handler

import com.foodback.core.coreCommon.api.error.GlobalErrorResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice
internal class AuthExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<GlobalErrorResponse> {
        val body = GlobalErrorResponse(
            error = IllegalArgumentException::class.java.simpleName,
            message = "Illegal argument exception",
            httpCode = HttpStatus.BAD_REQUEST.value(),
        )

        return ResponseEntity.badRequest()
            .body(body)
    }
}