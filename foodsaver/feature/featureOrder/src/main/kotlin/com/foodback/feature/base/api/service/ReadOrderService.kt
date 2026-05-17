package com.foodback.feature.base.api.service

import com.foodback.feature.base.api.dto.OrderResponse
import java.util.UUID

interface ReadOrderService {

    fun getOrders(userId: UUID): List<OrderResponse>
}