package com.foodback.dto.request.cart

import java.time.Instant
import java.util.*

/**
 * Special DTO request to add product to server
 * @param title Name of this product
 * @param description Description of this product
 * @param cost Price of this product
 * @param organizationId Organization id, realizing this product
 * @param expiresAt Expiration date expires
 * @param addedAt Date when this product added to server
 * @param categoryIds Ids of product categories
 * @param count The count of products in storage
 */
data class ProductRequestModel(
    val title: String,
    val description: String,
    val cost: Float,
    val organizationId: UUID,
    val categoryIds: List<UUID>,
    val count: Long = 1,

    val unit: Long,
    val unitName: String,

    val expiresAt: Instant? = Instant.now(),
    val addedAt: Instant? = Instant.now()
)
