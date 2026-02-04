package com.foodback.dto.response.cart

import java.util.UUID

data class CartResponseModel(
    val id: UUID,
    val product: ProductResponseModel,
    val quantity: Long,
    val tempId: UUID
)
