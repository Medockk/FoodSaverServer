package com.foodback.feature.address.impl.exception

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class AddressNotFoundException(message: String = ""): GlobalError(message) {

    override val customCode: ServerErrorCode = AddressErrorCode.ADDRESS_NOT_FOUND
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
}