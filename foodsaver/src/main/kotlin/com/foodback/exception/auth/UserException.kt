package com.foodback.exception.auth

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * Global exception with working to user
 */
class UserException : GlobalError {

    override val message: String
    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String): super(message) {
        this.message = message
        this.httpStatus = HttpStatus.NOT_FOUND
        this.customCode = RequestError.UserRequest.USER_NOT_FOUND
    }

    constructor(message: String, customCode: ErrorCode): super(message) {
        this.message = message
        this.customCode = customCode
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    }

    constructor(message: String, httpStatus: HttpStatus): super(message) {
        this.message = message
        this.httpStatus = httpStatus
        this.customCode = RequestError.UserRequest.UNKNOWN_ERROR
    }

    constructor(message: String, httpStatus: HttpStatus, customCode: ErrorCode): super(message) {
        this.message = message
        this.httpStatus = httpStatus
        this.customCode = customCode
    }
}