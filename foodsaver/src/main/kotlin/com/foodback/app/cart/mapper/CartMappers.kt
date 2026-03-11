package com.foodback.app.cart.mapper

import com.foodback.app.cart.dto.request.CartRequestModel
import com.foodback.app.cart.dto.response.CartResponseModel
import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.app.cart.entity.CartEntity
import com.foodback.app.cart.entity.CartItemEntity
import com.foodback.app.common.dto.response.OrganizationResponseV1
import org.springframework.stereotype.Component
import java.util.*

// Methods to convert Request to entity and vice versa

@Component
class CartMapperV1 {
    fun mapToEntity(cartRequestModel: CartRequestModel, uid: UUID) = with(cartRequestModel) {
        CartEntity(
            uid = uid,
            items = mutableListOf()
        )
    }

    fun mapToProductResponse(cartItemEntity: CartItemEntity) = with(cartItemEntity) {
        ProductResponseModel(
            productId = product.id,
            title = product.title,
            description = product.description,
            cost = product.cost,
            rating = product.rating,
            organization = product.organization!!.let {
                OrganizationResponseV1(
                    id = it.id!!,
                    organizationName = it.organizationName,
                    owner = it.owner,
                    createdAt = it.createdAt!!
                )
            },
            count = quantity,
            expiresAt = product.expiresAt!!,
            categoryIds = emptyList(),
            photoUrl = product.photoUrl,
            unit = product.unit,
            unitName = product.unitName,
            enterpriseId = product.enterprise!!.id!!,
            costUnit = product.costUnit
        )
    }

    fun mapToCartResponse(cartItemEntity: CartItemEntity) = with(cartItemEntity) {
        CartResponseModel(
            id = id!!,
            product = mapToProductResponse(cartItemEntity),
            quantity = quantity,
            tempId = tempId!!
        )
    }
}
