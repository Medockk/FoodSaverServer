package com.foodback.feature.cart.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "cart_items")
internal class CartItemEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    val productId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    val cart: CartEntity? = null,

    @Column(nullable = false)
    @Min(value = 1L)
    var quantity: Long = 1L,

    @CreatedDate
    val addedAt: Instant = Instant.now()

)