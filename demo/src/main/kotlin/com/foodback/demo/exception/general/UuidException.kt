package com.foodback.demo.exception.general

import com.foodback.demo.exception.general.Error.GlobalError
import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import com.foodback.demo.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * Special exception, extended [com.foodback.demo.exception.general.Error.GlobalError], to use when [String] failed to parse to [java.util.UUID]
 */
class UuidException : GlobalError {

    override val message: String
    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String = "Failed to parse Uuid") {
        this.customCode = RequestError.Uuid.FAILED_PARSE_UUID
        this.message = message
        this.httpStatus = HttpStatus.BAD_REQUEST
    }
}