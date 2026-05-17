package com.foodback.feature.featureProduct.api.dto

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class AddProductRequest(
    val name: String,
    val description: String,

    val imageUris: List<String>,
    val expiresAt: Instant,

    val price: BigDecimal,
    val count: Long,

    val unit: String,
    @JvmField
    val discount: BigDecimal,

    val currency: Currencies,
    @JvmField
    val isAvailable: Boolean,

    val restaurantId: UUID,
    val ingredientIds: List<UUID>,
    val categoryIds: List<UUID>
)
