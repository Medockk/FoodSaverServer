package com.foodback.feature.address.api.dto

data class AddAddressRequest(
    val name: String,

    val latitude: Double,
    val longitude: Double,

    val city: String,
    val street: String,
    val house: String,

    val apartment: String?,
    val floor: Int?,
    val entrance: String?,
)
