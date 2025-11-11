package com.foodback.demo.exception.auth

import com.foodback.demo.exception.general.Error.GlobalError
import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import com.foodback.demo.exception.general.ErrorCode.RequestError
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

    constructor() : this(message = "User not found!")
    constructor(message: String, customCode: ErrorCode): super(message) {
        this.message = message
        this.customCode = customCode
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    }
}