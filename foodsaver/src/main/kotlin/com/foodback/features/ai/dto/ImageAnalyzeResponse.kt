package com.foodback.features.ai.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageAnalyzeResponse(
    val label: String,
    @JsonProperty("is_fresh")
    val isFresh: Boolean,
    val confidence: Double,
    val action: String
)
