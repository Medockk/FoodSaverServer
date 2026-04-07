package com.foodback.app.ingredients.mappers

import com.foodback.app.ingredients.dto.response.IngredientResponseModel
import com.foodback.app.ingredients.entity.IngredientsEntity
import org.springframework.stereotype.Component

@Component
class IngredientMapper {

    fun mapToResponse(ingredientsEntity: IngredientsEntity) = with(ingredientsEntity) {
        IngredientResponseModel(
            id = id!!,
            productId = product!!.id!!,
            ingredients = ingredients
        )
    }
}