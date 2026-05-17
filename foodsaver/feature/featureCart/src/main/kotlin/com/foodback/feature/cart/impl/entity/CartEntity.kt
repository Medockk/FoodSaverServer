package com.foodback.feature.cart.impl.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "carts")
@EntityListeners(AuditingEntityListener::class)
internal class CartEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val userId: UUID? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        mappedBy = "cart",
        orphanRemoval = true
    )
    var cartItems: MutableList<CartItemEntity> = mutableListOf(),

    @Column(nullable = false)
    var quantity: Long = 0L,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val addedAt: Instant = Instant.now()
)