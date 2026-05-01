package com.foodback.feature.featureRestaurant.impl.entity

import jakarta.persistence.Embeddable

@Embeddable
internal class AddressEntity(
    var addressName: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)