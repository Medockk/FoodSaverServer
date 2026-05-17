package com.foodback.feature.featureIngredients.impl.exception

import com.foodback.core.coreCommon.api.error.GlobalError
import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode
import org.springframework.http.HttpStatus

internal class IngredientNotFoundException(message: String = ""): GlobalError(message) {

    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
    override val customCode: ServerErrorCode = IngredientsErrorCode.INGREDIENT_NOT_FOUND
}