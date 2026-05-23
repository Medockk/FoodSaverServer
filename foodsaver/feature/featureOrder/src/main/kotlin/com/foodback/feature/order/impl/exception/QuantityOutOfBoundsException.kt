package com.foodback.feature.order.impl.exception

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class QuantityOutOfBoundsException(message: String = ""): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
    override val customCode: ServerErrorCode = OrderErrorCode.QUANTITY_OUT_OF_BOUNDS
}