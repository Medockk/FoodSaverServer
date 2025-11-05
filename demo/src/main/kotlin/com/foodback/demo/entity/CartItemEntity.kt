package com.foodback.demo.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "cart_item_entity")
data class CartItemEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "cart_id", nullable = false)
    var cart: CartEntity,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "product_id", nullable = false)
    var product: ProductEntity,

    @Column(nullable = false)
    var quantity: Int = 1
)
