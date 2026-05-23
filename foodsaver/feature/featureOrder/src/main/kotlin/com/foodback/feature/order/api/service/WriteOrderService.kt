package com.foodback.feature.order.api.service

import com.foodback.feature.order.api.dto.OrderResponse
import java.util.UUID

interface WriteOrderService {

    fun createOrder(userId: UUID): List<OrderResponse>
}