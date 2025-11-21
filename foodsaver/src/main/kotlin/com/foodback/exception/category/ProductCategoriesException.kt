package com.foodback.exception.category

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

/**
 * General exception to work with categories of products
 */
class ProductCategoriesException: GlobalError {

    override val httpStatus: HttpStatus
    override val customCode: ErrorCode

    constructor(): super("") {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = RequestError.ProductCategoriesRequest.UNKNOWN_ERROR
    }
    constructor(message: String): super(message) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        this.customCode = RequestError.ProductCategoriesRequest.UNKNOWN_ERROR
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