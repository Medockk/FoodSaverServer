package com.foodback.demo.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "cart")
data class CartEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var uid: String,

    @Column(nullable = false)
    var productCount: Int = 0,

    var createdAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        unique = false,
        nullable = false
    )
    var productId: ProductEntity
)
