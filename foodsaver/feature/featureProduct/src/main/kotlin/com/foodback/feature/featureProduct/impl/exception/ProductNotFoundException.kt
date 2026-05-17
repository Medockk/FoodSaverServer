package com.foodback.feature.featureProduct.impl.exception

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class ProductNotFoundException(message: String = ""): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
    override val customCode: ServerErrorCode = ProductErrorCode.PRODUCT_NOT_FOUND
}