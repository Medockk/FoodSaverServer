package com.foodback.feature.featureProduct.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import org.springframework.context.annotation.Configuration

@Configuration
internal class ProductErrorCodeProvider: ServerErrorCodeProvider {
    override fun getAllCodes(): List<ServerErrorCode> {
        return ProductErrorCode.entries.toList()
    }
}