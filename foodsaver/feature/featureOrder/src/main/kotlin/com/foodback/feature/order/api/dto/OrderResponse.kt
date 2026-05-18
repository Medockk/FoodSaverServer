package com.foodback.feature.order.api.dto

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val type: Type,
    val status: Status,
    val restaurantImageUri: String?,
    val restaurantName: String,
    val orderPrice: BigDecimal,
    val orderSize: Int,
    val trackNumber: String,
    val createdAt: Instant,

    val items: List<OrderItemResponse>
) {
    enum class Type {
        FOOD, DRINK
    }

    enum class Status {
        CREATED, COOKING, DELIVERED, CANCELLED
    }
}
