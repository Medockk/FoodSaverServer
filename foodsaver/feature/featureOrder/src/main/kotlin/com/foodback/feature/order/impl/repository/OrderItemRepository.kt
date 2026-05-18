package com.foodback.feature.order.impl.repository

import com.foodback.feature.order.impl.entity.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface OrderItemRepository: JpaRepository<OrderItemEntity, UUID> {

    fun findAllByOrderId(orderId: UUID): List<OrderItemEntity>
}