package com.foodback.demo.mappers

import com.foodback.demo.dto.request.cart.ProductRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.entity.ProductEntity
import java.time.Instant

fun ProductRequestModel.toEntity() =
    ProductEntity(
        title = title,
        description = description,
        cost = cost,
        expiresAt = expiresAt,
        organization = organization
    )

fun ProductEntity.toResponseModel(count: Int) =
    ProductResponseModel(
        productId = id,
        title = title,
        description = description,
        cost = cost,
        rating = rating ?: 0f,
        organization = organization,
        expiresAt = expiresAt ?: Instant.now(),
        count = count
    )