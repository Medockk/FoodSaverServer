package com.foodback.feature.featureRestaurant.impl.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.locationtech.jts.geom.Point
import java.util.UUID

@Entity
@Table(name = "restaurants")
internal class RestaurantEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    var name: String = "",
    var description: String = "",

    @ElementCollection
    @CollectionTable(name = "restaurant_photo_uris")
    @Column(columnDefinition = "TEXT")
    var photoUris: MutableList<String> = mutableListOf(),

    var rating: Double? = null,
    var averageDeliveryTime: Double? = null,
    var deliveryCost: Double? = null,

    @Column(columnDefinition = "geography(Point, 4326)")
    var coords: Point? = null,

    @Embedded
    @Column(name = "address")
    var address: AddressEntity? = null,

    @Column(name = "company_id")
    var companyId: UUID? = null,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isSuggested: Boolean = false,
)