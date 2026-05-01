package com.foodback.feature.users.impl.exception

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class UserNotFoundException(message: String = ""): GlobalError(message) {
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
    override val customCode: ServerErrorCode = AuthErrorCode.USER_NOT_FOUND
}