package com.foodback.exception.auth

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * Global authentication exception
 */
class AuthenticationException : GlobalError {

    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String) : super(message) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = RequestError.Authentication.UNKNOWN_ERROR
    }

    constructor(customCode: ErrorCode): super(customCode.javaClass.simpleName) {
        this.httpStatus = HttpStatus.resolve(customCode.code) ?: HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = customCode
    }

    constructor(message: String, customCode: ErrorCode): super(message) {
        this.customCode = customCode
        this.httpStatus = HttpStatus.resolve(customCode.code) ?: HttpStatus.INTERNAL_SERVER_ERROR
    }

    constructor(message: String, customCode: ErrorCode, httpStatus: HttpStatus): super(message) {
        this.customCode = customCode
        this.httpStatus = httpStatus
    }
}