package com.foodback.app.product.mapper

import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.app.product.dto.request.ProductRequestModel
import com.foodback.app.product.entity.CategoryEntity
import com.foodback.app.product.entity.OrganizationEntity
import com.foodback.app.product.entity.ProductEntity
import com.foodback.utils.http.HttpAddressUtil
import com.foodback.utils.http.PhotoUrlUtil
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.time.toKotlinDuration

// Methods to convert Request to entity and vice versa

@Component
class ProductMapperV1(
    private val photoUrlUtil: PhotoUrlUtil
) {

    fun mapToResponse(productEntity: ProductEntity) = with(productEntity) {
        var newPrice: Float = cost
        val expiresDuration = Instant.now().until(expiresAt).toKotlinDuration()
        val days = expiresDuration.inWholeDays
        if (days == 0L) {
            val hours = expiresDuration.inWholeHours
            newPrice = when (hours) {
                in 12L..24L -> cost
                in 6L..12L -> cost / 2
                in 3L..6L -> cost / 3
                else -> cost / 4
            }
        } else {
            newPrice = when (days) {
                in 4L..7L -> cost / 1.2f
                in 2L..3L -> cost / 1.5f
                1L -> cost / 2f
                else -> cost
            }
        }
        val photoUrl = this.photoUrl?.let { url ->
            photoUrlUtil.mapRelativeUrlToAbsoluteUrl(url)
        }

        ProductResponseModel(
            productId = id,
            title = title,
            description = description,
            cost = newPrice,
            oldCost = if (newPrice == cost) null
            else cost,
            rating = rating ?: 0f,
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
        count = count,
        categories = categoryIds.map { id -> CategoryEntity(id = id) }.toMutableList(),
        unit = unit,
        unitName = unitName,
    )
}