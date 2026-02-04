package com.foodback.exception.offer

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

class OfferException: GlobalError {

    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String): super(message) {
        this.httpStatus = HttpStatus.NOT_FOUND
        this.customCode = RequestError.OfferRequest.PRODUCT_NOT_FOUND
    }

    constructor(message: String, httpStatus: HttpStatus, customCode: ErrorCode) : super(message) {
        this.httpStatus = httpStatus
        this.customCode = customCode
    }
}