package com.foodback.feature.paymentMethod.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "payment_method_types")
@EntityListeners(AuditingEntityListener::class)
internal class PaymentMethodTypeEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    var name: String = "",
    var iconUri: String? = null,

    @CreatedDate
    @Column(nullable = false)
    val addedAt: Instant = Instant.now()
)