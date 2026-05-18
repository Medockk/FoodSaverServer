package com.foodback.feature.order.impl.service

import com.foodback.feature.order.api.dto.OrderResponse
import com.foodback.feature.order.api.service.ReadOrderService
import com.foodback.feature.order.impl.mapper.OrderMapper
import com.foodback.feature.order.impl.repository.OrderRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
internal class ReadOrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderMapper: OrderMapper
): ReadOrderService {

    override fun getOrders(userId: UUID): List<OrderResponse> {
        val order = orderRepository
            .findAllByUserId(userId)

        return order.map {
            orderMapper.toResponse(it)
        }
    }
}