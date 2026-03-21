package com.foodback.dto.request.cart

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

/**
 * Special DTO request to add product to cart
 * @param productId id of current product
 * @param quantity count of this product
 */
data class CartRequestModel(
    @field:NotNull(message = "Product id cannot be null!")
    val productId: UUID,

    @field:Min(value = 1, message = "Min quantity 1")
    val quantity: Long = 1
)
