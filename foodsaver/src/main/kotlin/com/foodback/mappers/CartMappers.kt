package com.foodback.mappers

import com.foodback.dto.request.cart.CartRequestModel
import com.foodback.dto.response.cart.CartResponseModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.entity.CartEntity
import com.foodback.entity.CartItemEntity
import java.util.*

// Methods to convert Request to entity and vice versa

fun CartRequestModel.toEntity(
    uid: UUID
) = CartEntity(
    uid = uid,
    items = mutableListOf()
)

fun CartItemEntity.toProductResponse() =
    ProductResponseModel(
        productId = product.id,
        title = product.title,
        description = product.description,
        cost = product.cost,
        rating = product.rating,
        organization = requireNotNull(product.organization),
        count = quantity,
        expiresAt = product.expiresAt!!,
        categoryIds = emptyList(),
        photoUrl = product.photoUrl,
        unit = product.unit,
        unitName = product.unitName,
        costUnit = product.costUnit
    )

fun CartItemEntity.toCartResponse() =
    CartResponseModel(
        id = id!!,
        product = product.toResponseModel(),
        quantity = quantity,
        tempId = tempId!!
    )