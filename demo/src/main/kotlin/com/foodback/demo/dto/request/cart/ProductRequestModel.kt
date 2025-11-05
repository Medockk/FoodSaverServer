package com.foodback.demo.dto.request.cart

import java.time.Instant

/**
 * Special DTO request to add product to server
 * @param title Name of this product
 * @param description Description of this product
 * @param cost Price of this product
 * @param organization Organization, realizing this product
 * @param expiresAt Expiration date expires
 * @param addedAt Date when this product added to server
 */
data class ProductRequestModel(
    val title: String,
    val description: String,
    val cost: Float,
    val organization: String,

    val expiresAt: Instant? = Instant.now(),
    val addedAt: Instant? = Instant.now(),

)
