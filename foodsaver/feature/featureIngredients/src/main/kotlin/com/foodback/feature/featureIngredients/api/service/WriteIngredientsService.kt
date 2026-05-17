package com.foodback.feature.featureIngredients.api.service

import com.foodback.feature.featureIngredients.api.dto.AddIngredientRequest
import com.foodback.feature.featureIngredients.api.dto.IngredientResponse

interface WriteIngredientsService {

    fun addIngredient(request: AddIngredientRequest): IngredientResponse
}