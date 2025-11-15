package com.foodback.demo.mappers

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.entity.CartEntity
import com.foodback.demo.entity.CartItemEntity
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
        expiresAt = product.expiresAt!!
    )