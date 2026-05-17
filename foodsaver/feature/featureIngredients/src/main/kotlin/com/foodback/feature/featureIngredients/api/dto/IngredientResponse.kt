package com.foodback.feature.featureIngredients.api.dto

import java.util.UUID

data class IngredientResponse(
    val id: UUID,
    val name: String,
    val isAllergy: Boolean,
    val imageUri: String?
)
