package com.foodback.demo.exception.auth

import com.foodback.demo.exception.general.Error.GlobalError
import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import com.foodback.demo.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

class AuthenticationException : GlobalError {

    override val message: String
    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor() {
        this.message = ""
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = RequestError.Authentication.UNKNOWN_ERROR
    }

    constructor(customCode: ErrorCode) {
        this.message = customCode.javaClass.simpleName
        this.httpStatus = HttpStatus.resolve(customCode.code) ?: HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = customCode
    }

    constructor(message: String, customCode: ErrorCode) {
        this.message = message
        this.customCode = customCode
        this.httpStatus = HttpStatus.resolve(customCode.code) ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}