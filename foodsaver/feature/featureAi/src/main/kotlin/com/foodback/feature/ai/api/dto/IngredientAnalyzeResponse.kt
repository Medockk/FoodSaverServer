package com.foodback.feature.ai.api.dto

data class IngredientAnalyzeResponse(
    val name: String,
    val dangerLevel: String,
    val explanation: String
)
