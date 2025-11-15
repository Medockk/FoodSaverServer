package com.foodback.demo.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

/**
 * Special database entity
 * @param id unique identifier of product in format [UUID]
 * @param title Product name
 * @param description Description of current product
 * @param cost Cost of current product
 * @param rating 5-star rating
 * @param organization Organization, whose sell current product
 * @param addedAt Date, when current product added in system
 * @param expiresAt Date, when current product expiration
 */
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: OrganizationEntity? = null,

    @Column(nullable = true)
    var rating: Float? = null,

    var addedAt: Instant? = Instant.now(),
    var expiresAt: Instant? = Instant.now(),
)
