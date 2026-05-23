package com.foodback.feature.ai.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageAnalyzeResponse(
    val label: String,
    @JsonProperty("is_fresh")
    val isFresh: Boolean,
    val confidence: Double,
    val action: String
)
