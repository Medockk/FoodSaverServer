package com.foodback.demo.dto.request.cart

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CartRequestModel(
    @field:NotNull(message = "Product id cannot be null!")
    val productId: UUID,

    @field:Min(value = 1, message = "Min quantity 1")
    val quantity: Int = 1
)
