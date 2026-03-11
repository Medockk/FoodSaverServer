package com.foodback.app.offers.dto.request

import java.util.UUID

data class OfferRequestModel(
    val title: String,
    val description: String?,
    val productId: UUID
)