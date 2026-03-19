package com.foodback.app.cart.repository

import com.foodback.app.cart.entity.CartEntity
import com.foodback.app.cart.entity.CartItemEntity
import com.foodback.app.product.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.*

/**
 * interface to access with table 'cart_item_entity' in database
 */
interface CartItemRepository : JpaRepository<CartItemEntity, UUID> {

    /**
     * Method to find [CartItemEntity] by [cart] and [product]
     * @return An [Optional] of [CartItemEntity]
     */
    fun findByCartAndProduct(cart: CartEntity, product: ProductEntity): Optional<CartItemEntity>

    /**
     * Method to find all user [CartItemEntity] by [cartEntity]
     * @return An [Optional] of [List] of [CartItemEntity]
     */
    fun findAllByCart(cartEntity: CartEntity): Optional<List<CartItemEntity>>

    fun findAllByProductExpiresAtBefore(threshold: Instant): Optional<List<CartItemEntity>>
}