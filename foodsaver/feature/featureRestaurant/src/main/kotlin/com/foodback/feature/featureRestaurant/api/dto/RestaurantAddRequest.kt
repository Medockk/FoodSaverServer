package com.foodback.feature.featureRestaurant.api.dto

data class RestaurantAddRequest(
    val name: String,
    val description: String,
    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,
    val photoUri: String? = null,
    // TODO add organization!!!
)
