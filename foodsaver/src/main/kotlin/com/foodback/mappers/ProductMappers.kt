package com.foodback.mappers

import com.foodback.dto.request.cart.ProductRequestModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.entity.CategoryEntity
import com.foodback.entity.OrganizationEntity
import com.foodback.entity.ProductEntity
import java.time.Instant
import kotlin.math.cos
import kotlin.time.toKotlinDuration

// Methods to convert Request to entity and vice versa

fun ProductRequestModel.toEntity(organization: OrganizationEntity): ProductEntity {
    return ProductEntity(
        title = title,
        description = description,
        cost = cost,
        expiresAt = expiresAt,
        organization = organization,
        count = count,
        categories = categoryIds.map { id -> CategoryEntity(id = id) }.toMutableList(),
        unit = unit,
        unitName = unitName,
    )
}

fun ProductEntity.toResponseModel(): ProductResponseModel {
    var newPrice: Float = cost
    val expiresDuration = Instant.now().until(expiresAt).toKotlinDuration()
    if (expiresDuration.inWholeDays == 0L) {
        val hours = expiresDuration.inWholeHours
        newPrice = when (hours) {
            in 12L..24L -> cost
            in 6L..12L -> cost / 2
            in 3L..6L -> cost / 3
            else -> cost / 4
        }
    } else {
        val days = expiresDuration.inWholeDays
        newPrice = when (days) {
            1L -> cost / 1.5f
            else -> cost
        }
    }

    return ProductResponseModel(
        productId = id,
        title = title,
        description = description,
        cost = newPrice,
        oldCost = if (newPrice == cost) null
        else cost,
        rating = rating ?: 0f,
        organization = requireNotNull(organization),
        expiresAt = expiresAt ?: Instant.now(),
        count = count,
        categoryIds = categories.map { requireNotNull(it.id) },
        photoUrl = photoUrl,
        unit = unit,
        unitName = unitName,
        costUnit = costUnit
    )
}