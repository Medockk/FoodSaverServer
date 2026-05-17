package com.foodback.feature.cart.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import org.springframework.context.annotation.Configuration

@Configuration
internal class CartErrorCodeProvider: ServerErrorCodeProvider {

    override fun getAllCodes(): List<ServerErrorCode> {
        return CartErrorCode.entries.toList()
    }
}