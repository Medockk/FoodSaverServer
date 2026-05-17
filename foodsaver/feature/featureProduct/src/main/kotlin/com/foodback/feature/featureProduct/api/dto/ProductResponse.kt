package com.foodback.feature.featureProduct.api.dto

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class ProductResponse(
    val id: UUID,

    val name: String,
    val description: String,

    val imageUris: List<String>,
    val expiresAt: Instant,

    val price: BigDecimal,
    val discount: BigDecimal,
    val count: Long,

    val unit: String,
    val currency: Currencies,

    val restaurantId: UUID,
    val ingredientIds: List<UUID>,
    val categoryIds: List<UUID>,

    val isDeleted: Boolean,
    val isAvailable: Boolean
)
