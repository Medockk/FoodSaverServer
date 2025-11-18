package com.foodback.exception.product

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * General exception to working with all products
 */
class ProductNotFoundException : GlobalError {

    override val message: String
    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String, customCode: ErrorCode, httpStatus: HttpStatus): super(message) {
        this.message = message
        this.customCode = customCode
        this.httpStatus = httpStatus
    }

    constructor(message: String = "Product not found") :
            this(message, RequestError.ProductRequest.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND)
}