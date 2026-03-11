package com.foodback.app.offers.dto.response

import java.util.UUID

data class OfferResponseModel(
    val id: UUID,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val productId: UUID
)