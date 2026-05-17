package com.foodback.feature.featureIngredients.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

enum class IngredientsErrorCode(override val code: Int): ServerErrorCode {

    INGREDIENT_NOT_FOUND(15_001)
}