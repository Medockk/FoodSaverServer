package com.foodback.demo.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "cart")
data class CartEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(unique = true, nullable = false)
    var uid: String,

    @Column(nullable = false)
    var productCount: Int = 0,

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "cart",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var items: MutableList<CartItemEntity> = mutableListOf(),

    var createdAt: Instant = Instant.now()
)
