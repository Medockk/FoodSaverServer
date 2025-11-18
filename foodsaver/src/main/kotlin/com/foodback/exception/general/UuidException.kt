package com.foodback.exception.general

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * Special exception, extended [com.foodback.demo.exception.general.Error.GlobalError], to use when [String] failed to parse to [java.util.UUID]
 */
class UuidException : GlobalError {

    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
    override val customCode: ErrorCode = RequestError.Uuid.FAILED_PARSE_UUID

    constructor(message: String = "Failed to parse Uuid"): super(message)
}