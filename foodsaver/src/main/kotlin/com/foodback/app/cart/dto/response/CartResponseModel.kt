package com.foodback.app.cart.dto.response

import com.foodback.app.common.dto.response.ProductResponseModel
import java.util.UUID

data class CartResponseModel(
    val id: UUID,
    val product: ProductResponseModel,
    val quantity: Long,
    val tempId: UUID
)