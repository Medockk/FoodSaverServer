package com.foodback.feature.address.api.dto

import java.util.UUID

data class AddressResponse(
    val id: UUID,
    val name: String,

    val latitude: Double,
    val longitude: Double,

    val city: String,
    val street: String,
    val house: String,

    val apartment: String?,
    val floor: Int?,
    val entrance: String?,

    val fullAddress: String
)
