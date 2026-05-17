package com.foodback.feature.cart.api.dto

import java.util.UUID

data class ChangeQuantityRequest(
    val cartItemId: UUID,
    val newQuantity: Long
)
