package com.foodback.feature.featureProduct.api.dto

import java.util.UUID

data class SearchProductsRequest(
    val query: String,
    val discoveredIds: List<UUID>,
)
