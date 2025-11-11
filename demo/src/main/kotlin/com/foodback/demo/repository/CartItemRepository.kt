package com.foodback.demo.repository

import com.foodback.demo.entity.CartEntity
import com.foodback.demo.entity.CartItemEntity
import com.foodback.demo.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
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
}