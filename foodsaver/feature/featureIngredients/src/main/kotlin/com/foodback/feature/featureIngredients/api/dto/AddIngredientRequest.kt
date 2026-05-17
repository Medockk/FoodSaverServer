package com.foodback.feature.featureIngredients.api.dto

data class AddIngredientRequest(
    val name: String,
    val isAllergy: Boolean,
    val imageUri: String
)
