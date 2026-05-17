package com.foodback.feature.cart.impl.exception

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class CartNotFoundException(message: String = ""): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
    override val customCode: ServerErrorCode = CartErrorCode.CART_NOT_FOUND
}