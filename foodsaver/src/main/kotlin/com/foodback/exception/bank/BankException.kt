package com.foodback.exception.bank

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

class BankException(
    override val message: String,
    override val httpStatus: HttpStatus,
    override val customCode: ErrorCode
): GlobalError(message) {

    constructor(message: String): this(
        message = message,
        HttpStatus.BAD_REQUEST,
        RequestError.PaymentRequest.PAYMENT_METHOD_NOT_FOUND
    )
}