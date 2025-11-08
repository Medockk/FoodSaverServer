package com.foodback.demo.exception.auth

import com.foodback.demo.exception.general.Error.GlobalError
import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import com.foodback.demo.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * User not found Exception
 */
class UserNotFoundException : GlobalError {

    override val message: String
    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String) {
        this.message = message
        this.httpStatus = HttpStatus.NOT_FOUND
        this.customCode = RequestError.UserRequest.USER_NOT_FOUND
    }

    constructor() : this(message = "User not found!")
}