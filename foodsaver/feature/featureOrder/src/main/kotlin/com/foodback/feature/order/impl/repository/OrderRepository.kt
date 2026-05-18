package com.foodback.feature.order.impl.repository

import com.foodback.feature.order.impl.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface OrderRepository: JpaRepository<OrderEntity, UUID> {

    fun findByUserId(userId: UUID): OrderEntity?
    fun findAllByUserId(userId: UUID): List<OrderEntity>
}