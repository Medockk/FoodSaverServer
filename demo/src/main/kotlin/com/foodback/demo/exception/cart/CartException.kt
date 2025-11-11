package com.foodback.demo.exception.cart

import com.foodback.demo.exception.general.Error.GlobalError
import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import com.foodback.demo.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

class CartException : GlobalError {

    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String): super(message) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = RequestError.CartRequest.CART_NOT_FOUND
    }

    constructor(customCode: ErrorCode, message: String): super(message) {
        this.customCode = customCode
        this.httpStatus = HttpStatus.resolve(customCode.code) ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}