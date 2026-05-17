package com.foodback.feature.featureIngredients.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import org.springframework.context.annotation.Configuration

@Configuration
internal class IngredientsErrorCodeProvider: ServerErrorCodeProvider {

    override fun getAllCodes(): List<ServerErrorCode> {
        return IngredientsErrorCode.entries.toList()
    }
}