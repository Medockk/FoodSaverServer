package com.foodback.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
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
 * @param count The count of current product in storage
 * @param categories A Categories of current product
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

    @Column(nullable = false, updatable = true)
    @ColumnDefault(value = "1")
    var count: Int = 0,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_category_link",
        joinColumns = [JoinColumn("product_id")],
        inverseJoinColumns = [JoinColumn("category_id")]
    )
    var categories: MutableList<ProductCategories> = mutableListOf(),

    var addedAt: Instant? = Instant.now(),
    var expiresAt: Instant? = Instant.now(),
)
