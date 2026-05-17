package com.foodback.feature.base.impl.service

import com.foodback.feature.base.api.dto.OrderResponse
import com.foodback.feature.base.api.service.ReadOrderService
import com.foodback.feature.base.impl.mapper.OrderMapper
import com.foodback.feature.base.impl.repository.OrderRepository
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