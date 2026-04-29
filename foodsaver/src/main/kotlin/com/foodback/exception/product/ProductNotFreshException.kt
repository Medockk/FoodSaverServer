package com.foodback.exception.product

import com.foodback.exception.general.Error.GlobalError
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import org.springframework.http.HttpStatus

class ProductNotFreshException(
    message: String
): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
    override val customCode: ErrorCode = RequestError.ProductRequest.PRODUCT_NOT_FRESH
}