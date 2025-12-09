package com.foodback.exception.payment

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

class PaymentMethodException: GlobalError {
    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String, httpStatus: HttpStatus, customCode: ErrorCode) : super(message) {
        this.httpStatus = httpStatus
        this.customCode = customCode
    }

    constructor(message: String) : super(message) {
        this.httpStatus = HttpStatus.BAD_REQUEST
        this.customCode = RequestError.PaymentRequest.PAYMENT_METHOD_NOT_FOUND
    }
}