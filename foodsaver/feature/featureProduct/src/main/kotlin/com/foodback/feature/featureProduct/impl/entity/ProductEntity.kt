package com.foodback.feature.featureProduct.impl.entity

import com.foodback.feature.featureProduct.api.dto.Currencies
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "products")
@EntityListeners(value = [AuditingEntityListener::class])
internal class ProductEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    // required fields
    var name: String = "",
    var description: String = "",

    @ElementCollection
    @CollectionTable(name = "product_image_uris")
    @Column(columnDefinition = "TEXT")
    var imageUris: MutableList<String> = mutableListOf(),
    var expiresAt: Instant = Instant.now(),

    @Column(precision = 19, scale = 2) // всего максимум 19 символов, включая до 2 копеек
    var price: BigDecimal = BigDecimal.ZERO,
    var count: Long = 0L,

    // pcs - штуки, kg/g/ml/l - кг/гр/мл/л
    var unit: String = "pcs",
    var discount: BigDecimal = BigDecimal.ZERO,

    @Enumerated(value = EnumType.STRING)
    var currency: Currencies = Currencies.RUB, // rub, usd, eur, kzt ( для казаха :) )
    var isAvailable: Boolean = true,

    @Column(nullable = false)
    var restaurantId: UUID? = null,

    @ElementCollection
    @CollectionTable(name = "product_ingredient_ids")
    var ingredientIds: MutableList<UUID> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "product_category_ids")
    var categoryIds: MutableList<UUID> = mutableListOf(),

    // optional fields
    @CreatedDate
    @Column(updatable = false)
    var createdAt: Instant = Instant.now(),

    var isDeleted: Boolean = false,

    @Version
    var version: Long = 0L,
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isSuggested: Boolean = false,
)