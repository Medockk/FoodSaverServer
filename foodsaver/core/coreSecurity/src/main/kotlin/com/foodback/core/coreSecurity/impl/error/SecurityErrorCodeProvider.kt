package com.foodback.core.coreSecurity.impl.error

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import org.springframework.context.annotation.Configuration

@Configuration
internal class SecurityErrorCodeProvider: ServerErrorCodeProvider {

    override fun getAllCodes(): List<ServerErrorCode> {
        return SecurityErrorCode.entries.toList()
    }
}