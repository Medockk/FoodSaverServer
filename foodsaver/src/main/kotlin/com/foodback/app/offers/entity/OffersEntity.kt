package com.foodback.app.offers.entity

import com.foodback.app.product.entity.ProductEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "offers")
data class OffersEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(nullable = false)
    var title: String = "",
    @Column(nullable = true)
    var description: String? = null,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var productEntity: ProductEntity? = null
)