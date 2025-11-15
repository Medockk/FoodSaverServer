package com.foodback.demo.exception

import com.foodback.demo.exception.general.Error.GlobalError
import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import com.foodback.demo.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * Global error for working with Organizations
 */
class OrganizationException: GlobalError {

    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String): super(message) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = RequestError.OrganizationRequest.UNKNOWN_ERROR
    }

    constructor(message: String, customCode: ErrorCode): super(message) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = customCode
    }

    constructor(message: String, customCode: ErrorCode, httpStatus: HttpStatus): super(message) {
        this.httpStatus = httpStatus
        this.customCode = customCode
    }
}