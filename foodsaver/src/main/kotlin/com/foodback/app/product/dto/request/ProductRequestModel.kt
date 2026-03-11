package com.foodback.app.product.dto.request

import java.time.Instant
import java.util.UUID

/**
 * Special DTO request to add product to server
 * @param title Name of this product
 * @param description Description of this product
 * @param cost Price of this product
 * @param expiresAt Expiration date expires
 * @param addedAt Date when this product added to server
 * @param categoryIds Ids of product categories
 * @param count The count of products in storage
 */
data class ProductRequestModel(
    val title: String,
    val description: String,
    val cost: Float,
    val costUnit: String,
    val categoryIds: List<UUID>,
    val count: Long = 1,

    val unit: Long,
    val unitName: String,

    val expiresAt: Instant? = Instant.now(),
    val addedAt: Instant? = Instant.now()
)