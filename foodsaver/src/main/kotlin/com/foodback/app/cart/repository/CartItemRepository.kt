package com.foodback.app.cart.repository

import com.foodback.app.cart.entity.CartEntity
import com.foodback.app.cart.entity.CartItemEntity
import com.foodback.app.product.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

    @Query("""
        SELECT item FROM CartItemEntity item
        JOIN FETCH item.cart cart
        JOIN FETCH cart.user user
        LEFT JOIN FETCH user.fcmTokensEntity
        JOIN FETCH item.product product
        WHERE product.expiresAt < :threshold
        AND NOT EXISTS (
            SELECT log FROM NotificationLogEntity log
            WHERE log.uid = user.uid AND log.product = product
        )"""
    )
    fun findAllExpiredToNotify(threshold: Instant): List<CartItemEntity>
}