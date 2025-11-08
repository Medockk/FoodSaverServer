package com.foodback.demo.exception.product

import com.foodback.demo.exception.general.Error.GlobalError
import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import com.foodback.demo.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * If product not found in database
 */
class ProductNotFoundException : GlobalError {

    override val message: String
    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(message: String, customCode: ErrorCode, httpStatus: HttpStatus) {
        this.message = message
        this.customCode = customCode
        this.httpStatus = httpStatus
    }

    constructor(message: String = "Product not found") :
            this(message, RequestError.ProductRequest.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND)
}