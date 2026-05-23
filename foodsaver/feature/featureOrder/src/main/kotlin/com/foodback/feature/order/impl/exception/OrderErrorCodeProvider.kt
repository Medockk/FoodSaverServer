package com.foodback.feature.order.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import org.springframework.context.annotation.Configuration

@Configuration
class OrderErrorCodeProvider: ServerErrorCodeProvider {

    override fun getAllCodes(): List<ServerErrorCode> {
        return listOf(
            OrderErrorCode.QUANTITY_OUT_OF_BOUNDS
        )
    }
}