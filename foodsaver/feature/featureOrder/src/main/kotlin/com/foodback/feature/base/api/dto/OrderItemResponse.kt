package com.foodback.feature.base.api.dto

import java.math.BigDecimal
import java.util.UUID

data class OrderItemResponse(
    val id: UUID,
    val productId: UUID,
    val name: String,
    val price: BigDecimal,
    val quantity: Long
)
