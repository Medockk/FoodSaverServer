package com.foodback.app.product.service

import java.util.UUID

data class PersonalizationInputData(
    val uid: UUID,
    val searchType: SearchType,
    val searchRadiusKm: Double = 3.0
) {
    data class UserLocation(
        val latitude: Double?,
        val longitude: Double?
    )

    enum class SearchType {
        NEARBY,
        RECOMMENDED
    }
}
