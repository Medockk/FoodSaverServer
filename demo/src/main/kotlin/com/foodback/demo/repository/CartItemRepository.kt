package com.foodback.demo.repository

import com.foodback.demo.entity.CartEntity
import com.foodback.demo.entity.CartItemEntity
import com.foodback.demo.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CartItemRepository: JpaRepository<CartItemEntity, UUID> {

    fun findByCartAndProduct(cart: CartEntity, product: ProductEntity): Optional<CartItemEntity>
    fun findAllByCart(cartEntity: CartEntity): Optional<List<CartItemEntity>>
}