package com.foodback.feature.cart.impl.repository

import com.foodback.feature.cart.impl.entity.CartEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

internal interface CartRepository: JpaRepository<CartEntity, UUID> {

    fun findByUserId(userId: UUID): CartEntity?
    fun findByUserIdAndCartItemsId(userId: UUID, cartItemId: UUID): Optional<CartEntity>
}