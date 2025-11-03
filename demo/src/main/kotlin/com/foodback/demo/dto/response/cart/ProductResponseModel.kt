package com.foodback.demo.dto.response.cart

import java.time.Instant
import java.util.UUID

data class ProductResponseModel(
    val productId: UUID?,
    val title: String,
    val description: String,

    val cost: Float,
    val rating: Float,
    val organization: String,
    val count: Int,

    val expiresAt: Instant = Instant.now(),
)
