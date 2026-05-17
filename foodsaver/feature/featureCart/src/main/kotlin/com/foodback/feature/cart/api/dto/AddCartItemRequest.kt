package com.foodback.feature.cart.api.dto

import java.util.UUID

data class AddCartItemRequest(
    val productId: UUID,
    val quantity: Long = 1L
)
