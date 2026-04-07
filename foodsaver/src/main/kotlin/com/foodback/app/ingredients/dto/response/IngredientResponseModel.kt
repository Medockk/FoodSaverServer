package com.foodback.app.ingredients.dto.response

import java.util.*

data class IngredientResponseModel(
    val id: UUID,
    val productId: UUID,
    val ingredients: List<String>
)