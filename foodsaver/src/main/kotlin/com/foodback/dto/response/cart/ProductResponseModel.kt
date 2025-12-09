package com.foodback.dto.response.cart

import com.foodback.entity.OrganizationEntity
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
 * @param count Count of current product in storage OR count of current in cart
 * @param expiresAt Expiration date expires
 * @param categoryIds The Ids of all product categories
 */
data class ProductResponseModel(
    val productId: UUID?,

    val title: String,
    val description: String,
    val photoUrl: String?,

    val cost: Float,
    val costUnit: String,
    val oldCost: Float? = null,

    val count: Long,
    val rating: Float?,
    val organization: OrganizationEntity,
    val categoryIds: List<UUID>,

    val unit: Long,
    val unitName: String,

    val expiresAt: Instant = Instant.now(),
)
