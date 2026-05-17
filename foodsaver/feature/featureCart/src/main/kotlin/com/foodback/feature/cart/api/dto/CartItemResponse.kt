package com.foodback.feature.cart.api.dto

import java.time.Instant
import java.util.UUID

data class CartItemResponse(
    val id: UUID,
    val productId: UUID,
    val quantity: Long,
    val addedAt: Instant,
    val attributes: Attributes? = Attributes()
) {
    data class Attributes(
        val size: String? = null,
        val additions: List<String> = emptyList()
    )
}
