package com.foodback.feature.featureRestaurant.api.dto

import java.util.UUID

data class UpdateRestaurantRequest(
    val restaurantId: UUID,

    val name: String? = null,
    val description: String? = null,
    val photoUris: List<String>? = null,

    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,

    val address: RestaurantAddressResponse? = null
)
