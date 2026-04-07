package com.foodback.exception.handler

import com.foodback.exception.auth.AuthenticationException
import com.foodback.exception.general.Error.GlobalErrorResponse
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(1)
@RestControllerAdvice
class AuthenticationExceptionHandler {

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<GlobalErrorResponse> {
        println("handleAuthenticationException customCode is ${e.customCode}")
        println("handleAuthenticationException httpStatus is ${e.httpStatus}")
        e.printStackTrace()
        val status = when (e.customCode) {
            RequestError.Authentication.USERNAME_OCCUPIED -> HttpStatus.CONFLICT
            RequestError.Authentication.FAILED_AUTHORIZE_USER,
            RequestError.Authentication.FAILED_REGISTER_USER,
            RequestError.Authentication.FAILED_TO_VERIFY_GOOGLE_ID -> HttpStatus.BAD_REQUEST
            RequestError.Authentication.JWT_TOKEN_EXPIRED,
            RequestError.Authentication.REFRESH_TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED

            RequestError.Authentication.USER_NOT_AUTHORIZED -> HttpStatus.NOT_FOUND
            RequestError.Authentication.PASSWORD_NOT_EQUALS -> HttpStatus.BAD_REQUEST
            RequestError.Authentication.RESET_TOKEN_NOT_FOUND,
            RequestError.Authentication.RESET_TOKEN_LINKED_TO_NULL -> HttpStatus.NOT_FOUND
            else -> HttpStatus.BAD_REQUEST
        }

        return ResponseEntity
            .status(status)
            .body(GlobalErrorResponse(
                error = e::class.java.simpleName,
                message = e.message,
                httpCode = status.value(),
                errorCode = e.customCode.code
            ))
    }
}