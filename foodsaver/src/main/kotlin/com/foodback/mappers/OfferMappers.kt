package com.foodback.mappers

import com.foodback.dto.response.offer.OfferResponseModel
import com.foodback.entity.OffersEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OfferMappers(
    @Value($$"${server.address}")
    private val serverAddress: String,
    @Value($$"${server.port}")
    private val serverPort: String,
    @Value($$"${server.protocol}")
    private val serverProtocol: String,
) {

    fun OffersEntity.toResponseModel() =
        OfferResponseModel(
            id = id!!,
            title = title,
            description = description,
            productId = productEntity?.id!!,
            imageUrl = "$serverProtocol://$serverAddress:$serverPort/${this.imageUrl}"
        )

    fun List<OffersEntity>.toResponseModel() =
        this.map { it.toResponseModel() }
}
