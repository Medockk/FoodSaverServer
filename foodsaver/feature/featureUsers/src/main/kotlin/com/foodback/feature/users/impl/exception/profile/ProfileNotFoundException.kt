package com.foodback.feature.users.impl.exception.profile

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class ProfileNotFoundException(message: String = ""): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
    override val customCode: ServerErrorCode = ProfileErrorCode.PROFILE_NOT_FOUND
}