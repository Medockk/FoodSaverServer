package com.foodback.dto.request.offer

import java.util.UUID

data class OfferRequestModel(
    val title: String,
    val description: String?,
    val productId: UUID
)
