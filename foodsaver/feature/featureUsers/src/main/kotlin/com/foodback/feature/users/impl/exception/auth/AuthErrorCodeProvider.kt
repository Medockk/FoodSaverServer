package com.foodback.feature.users.impl.exception.auth

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
internal class AuthErrorCodeProvider: ServerErrorCodeProvider {

    override fun getAllCodes(): List<ServerErrorCode> {
        return AuthErrorCode.entries.toList()
    }
}