package com.foodback.demo.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(nullable = false)
    var title: String,
    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var cost: Float,

    @Column(nullable = false)
    var organization: String,

    @Column(nullable = true)
    var rating: Float? = null,

    var addedAt: Instant? = Instant.now(),
    var expiresAt: Instant? = Instant.now(),
)
