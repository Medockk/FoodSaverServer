package com.foodback.feature.featureProduct.api.dto

import java.util.UUID

/**
 * Если [productId] == null, значит продукт ещё не создан ->
 * картинку помещаем в директорию temp
 */
data class UploadProductImageRequest(
    val image: ByteArray,
    val productId: UUID?,
    val restaurantId: UUID,
    val imageExtension: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UploadProductImageRequest

        if (!image.contentEquals(other.image)) return false
        if (productId != other.productId) return false
        if (restaurantId != other.restaurantId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = image.contentHashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + restaurantId.hashCode()
        return result
    }
}
