package com.foodback.dto.response.offer

import java.util.UUID

data class OfferResponseModel(
    val id: UUID,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val productId: UUID
)
