package com.foodback.feature.base.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "order_items")
internal class OrderItemEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    var productId: UUID,
    @Column(nullable = false)
    var name: String = "",
    @Column(nullable = false)
    var price: BigDecimal,
    @Column(nullable = false)
    var quantity: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("order_id")
    var order: OrderEntity? = null
)