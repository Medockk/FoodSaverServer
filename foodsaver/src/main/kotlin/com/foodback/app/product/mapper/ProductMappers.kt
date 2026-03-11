package com.foodback.app.product.mapper

import com.foodback.app.common.dto.response.OrganizationResponseV1
import com.foodback.app.product.dto.request.ProductRequestModel
import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.app.product.entity.CategoryEntity
import com.foodback.app.product.entity.OrganizationEntity
import com.foodback.app.product.entity.ProductEntity
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.time.toKotlinDuration

// Methods to convert Request to entity and vice versa

@Component
class ProductMapperV1 {
    fun mapToResponse(productEntity: ProductEntity) = with(productEntity) {
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

        ProductResponseModel(
            productId = id,
            title = title,
            description = description,
            cost = newPrice,
            oldCost = if (newPrice == cost) null
            else cost,
            rating = rating ?: 0f,
            organization = productEntity.organization!!.let {
                OrganizationResponseV1(
                    id = it.id!!,
                    organizationName = it.organizationName,
                    owner = it.owner,
                    createdAt = it.createdAt!!
                )
            },
            expiresAt = expiresAt ?: Instant.now(),
            count = count,
            categoryIds = categories.map { requireNotNull(it.id) },
            photoUrl = photoUrl,
            unit = unit,
            unitName = unitName,
            enterpriseId = enterprise!!.id!!,
            costUnit = costUnit
        )
    }
}

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