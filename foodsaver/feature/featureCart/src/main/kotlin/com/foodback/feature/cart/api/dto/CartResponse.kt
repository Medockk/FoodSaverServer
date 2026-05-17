package com.foodback.feature.cart.api.dto

import java.math.BigDecimal
import java.util.UUID

data class CartResponse(
    val id: UUID,
    val quantity: Long,
    val itemsPrice: BigDecimal,
    val discountPrice: BigDecimal,
    val deliveryPrice: BigDecimal,
    val finalPrice: BigDecimal
)
