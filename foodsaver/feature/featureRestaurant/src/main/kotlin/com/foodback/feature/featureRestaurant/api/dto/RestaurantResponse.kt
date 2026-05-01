package com.foodback.feature.featureRestaurant.api.dto

import java.util.UUID

data class RestaurantResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val rating: Double? = null,
    val deliveryCost: Double? = null,
    val averageDeliveryTime: Double? = null,
    val photoUris: List<String> = emptyList(),

    val latitude: Double,
    val longitude: Double,
    val addressName: String,
)
