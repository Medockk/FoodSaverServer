package com.foodback.demo.mappers

import com.foodback.demo.dto.request.cart.ProductRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.entity.OrganizationEntity
import com.foodback.demo.entity.ProductEntity
import java.time.Instant

// Methods to convert Request to entity and vice versa

fun ProductRequestModel.toEntity(organization: OrganizationEntity): ProductEntity {
    return ProductEntity(
        title = title,
        description = description,
        cost = cost,
        expiresAt = expiresAt,
        organization = organization
    )
}

fun ProductEntity.toResponseModel(count: Int = 1) =
    ProductResponseModel(
        productId = id,
        title = title,
        description = description,
        cost = cost,
        rating = rating ?: 0f,
        organization = requireNotNull(organization),
        expiresAt = expiresAt ?: Instant.now(),
        count = count
    )