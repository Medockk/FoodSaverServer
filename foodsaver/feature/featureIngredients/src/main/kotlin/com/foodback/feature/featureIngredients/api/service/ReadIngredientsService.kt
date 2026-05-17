package com.foodback.feature.featureIngredients.api.service

import com.foodback.feature.featureIngredients.api.dto.IngredientResponse
import java.util.UUID

interface ReadIngredientsService {

    fun getIngredientById(ingredientId: UUID): IngredientResponse
}