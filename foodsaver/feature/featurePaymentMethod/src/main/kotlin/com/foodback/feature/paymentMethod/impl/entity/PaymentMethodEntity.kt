package com.foodback.feature.paymentMethod.impl.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@Table(name = "payment_methods")
@EntityListeners(AuditingEntityListener::class)
internal class PaymentMethodEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    val userId: UUID,

    @ManyToOne(fetch = FetchType.LAZY,)
    @JoinColumn(nullable = false, name = "type_id")
    val type: PaymentMethodTypeEntity? = null,

    // nullable for cash payment method
    var holderName: String? = null,
    var cartNumber: String? = null,
    var expiresDate: Instant? = null,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isSelected: Boolean = false,

    @Column(nullable = false)
    @CreatedDate
    val addedAt: Instant = Instant.now()
)