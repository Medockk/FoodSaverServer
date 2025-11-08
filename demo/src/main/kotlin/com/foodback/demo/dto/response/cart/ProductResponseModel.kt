package com.foodback.demo.dto.response.cart

import java.time.Instant
import java.util.*

/**
 * HTTP Response of product
 * @param productId Special unique identifier of current product
 * @param title Name current product
 * @param description Description of current product
 * @param cost Price of current product
 * @param rating 5-star ration of current product
 * @param organization Organization, releasing current product
 * @param count Count of current product
 * @param expiresAt Expiration date expires
 */
data class ProductResponseModel(
    val productId: UUID?,
    val title: String,
    val description: String,

    val cost: Float,
    val rating: Float?,
    val organization: String,
    val count: Int,

    val expiresAt: Instant = Instant.now(),
)
