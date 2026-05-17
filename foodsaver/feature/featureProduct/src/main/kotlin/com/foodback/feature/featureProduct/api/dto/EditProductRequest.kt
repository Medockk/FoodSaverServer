package com.foodback.feature.featureProduct.api.dto

import java.math.BigDecimal
import java.util.UUID

data class EditProductRequest(
    val id: UUID,
    val name: String?,
    val description: String?,

    val imageUris: List<String>?,

    val price: BigDecimal?,
    val discount: BigDecimal?,

    val count: Long?,
    val unit: String?,
    val currency: Currencies?,
    val isAvailable: Boolean?,
    val isDeleted: Boolean?,

    val ingredientIds: List<UUID>?,
    val categoryIds: List<UUID>?
)
