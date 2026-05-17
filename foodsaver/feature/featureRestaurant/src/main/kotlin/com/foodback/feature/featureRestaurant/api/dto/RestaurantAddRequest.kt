package com.foodback.feature.featureRestaurant.api.dto

import java.util.UUID

data class RestaurantAddRequest(
    val companyId: UUID,

    val name: String,
    val description: String,
    val photoUri: String,

    val rating: Double? = null,
    val averageDeliveryTime: Double? = null,
    val deliveryCost: Double? = null,

    val address: RestaurantAddressResponse
)
