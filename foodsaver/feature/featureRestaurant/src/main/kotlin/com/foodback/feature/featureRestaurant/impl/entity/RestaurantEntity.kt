package com.foodback.feature.featureRestaurant.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "restaurants")
internal class RestaurantEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String = "",
    var description: String = "",

    @ElementCollection
    var photoUris: MutableList<String> = mutableListOf(),

    var rating: Double? = null,
    var averageDeliveryTime: Double? = null,
    var deliveryCost: Double? = null,

    @Embedded
    @Column(name = "address")
    var address: AddressEntity? = null,

    @Column(name = "company_id")
    var companyId: UUID? = null,
)