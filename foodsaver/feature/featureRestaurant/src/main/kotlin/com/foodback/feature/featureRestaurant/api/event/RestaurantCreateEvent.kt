package com.foodback.feature.featureRestaurant.api.event

import java.util.UUID

data class RestaurantCreateEvent(
    val id: UUID,
    val name: String,
    val description: String,
    val rating: Double? = null,
    val deliveryCost: Double? = null,
    val averageDeliveryTime: Double? = null,
    val photoUri: String? = null,
    // TODO Add organization!!
)
