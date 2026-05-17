package com.foodback.feature.base.impl.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener::class)
internal class OrderEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    var userId: UUID,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<OrderItemEntity> = mutableListOf(),

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    var type: OrderType = OrderType.FOOD,

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.CREATED,

    var restaurantImageUri: String? = null,

    @Column(nullable = false)
    var restaurantName: String = "",

    @Column(nullable = false)
    var orderPrice: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var orderSize: Int = 0,

    @Column(nullable = false)
    var trackNumber: String = "",

    @CreatedDate
    val createdAt: Instant = Instant.now()
)