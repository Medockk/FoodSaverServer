package com.foodback.exception.address

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

class AddressException(
    override val message: String,
    override val httpStatus: HttpStatus,
    override val customCode: ErrorCode
) : GlobalError(message) {

    constructor(message: String): this(message, HttpStatus.BAD_REQUEST, RequestError.AddressRequest.ADDRESS_NOT_FOUND)

}