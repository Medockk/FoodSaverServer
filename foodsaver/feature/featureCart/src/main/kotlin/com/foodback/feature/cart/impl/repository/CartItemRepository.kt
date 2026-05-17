package com.foodback.feature.cart.impl.repository

import com.foodback.feature.cart.impl.entity.CartItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface CartItemRepository: JpaRepository<CartItemEntity, UUID> {

    fun findAllByCartId(cartId: UUID, pageable: Pageable): Page<CartItemEntity>
    fun findAllByCartId(cartId: UUID): List<CartItemEntity>
}