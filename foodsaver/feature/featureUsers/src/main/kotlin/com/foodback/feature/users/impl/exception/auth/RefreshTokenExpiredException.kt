package com.foodback.feature.users.impl.exception.auth

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class RefreshTokenExpiredException(message: String = ""): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
    override val customCode: ServerErrorCode = AuthErrorCode.REFRESH_TOKEN_EXPIRED
}