package com.foodback.app.cart.entity

import com.foodback.app.product.entity.ProductEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

/**
 * Special database entity - related-table of [cart] and [product]
 * @param id unique identifier in format [java.util.UUID]
 * @param cart A [CartEntity] table
 * @param product A [ProductEntity] table
 * @param quantity Quantity of current product in user cart
 */
@Entity
@Table(name = "cart_item_entity")
data class CartItemEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    var cart: CartEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: ProductEntity,

    @Column(nullable = false)
    var quantity: Long = 1,

    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(
        nullable = false
    )
    var tempId: UUID? = null
)