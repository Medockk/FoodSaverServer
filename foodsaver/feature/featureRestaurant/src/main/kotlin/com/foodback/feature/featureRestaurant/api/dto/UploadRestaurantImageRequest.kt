package com.foodback.feature.featureRestaurant.api.dto

import java.util.UUID

data class UploadRestaurantImageRequest(
    val restaurantId: UUID,
    val image: ByteArray,
    val imageExtension: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UploadRestaurantImageRequest

        if (restaurantId != other.restaurantId) return false
        if (!image.contentEquals(other.image)) return false
        if (imageExtension != other.imageExtension) return false

        return true
    }

    override fun hashCode(): Int {
        var result = restaurantId.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + (imageExtension?.hashCode() ?: 0)
        return result
    }
}
