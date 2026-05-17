package com.foodback.feature.cart.api.dto

import java.util.UUID

data class ProductInCartResponse(
    val productId: UUID,
    val cartItemId: UUID
)
