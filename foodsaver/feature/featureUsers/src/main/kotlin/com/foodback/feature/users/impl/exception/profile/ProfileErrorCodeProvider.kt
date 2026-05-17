package com.foodback.feature.users.impl.exception.profile

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import org.springframework.context.annotation.Configuration

@Configuration
internal class ProfileErrorCodeProvider: ServerErrorCodeProvider {

    override fun getAllCodes(): List<ServerErrorCode> {
        return ProfileErrorCode.entries.toList()
    }
}