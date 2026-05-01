package com.foodback.feature.users.impl.exception

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class UserAlreadyRegisteredException(message: String = ""): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
    override val customCode: ServerErrorCode = AuthErrorCode.USER_ALREADY_REGISTERED
}