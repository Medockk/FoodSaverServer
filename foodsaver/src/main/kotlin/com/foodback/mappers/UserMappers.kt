package com.foodback.mappers

import com.foodback.dto.response.auth.AuthResponse
import com.foodback.dto.response.address.AddressResponseModel
import com.foodback.dto.response.user.PaymentCardResponse
import com.foodback.dto.response.user.UserResponseModel
import com.foodback.entity.AddressEntity
import com.foodback.entity.PaymentMethodEntity
import com.foodback.entity.User.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

// Methods to convert Request to entity and vice versa

@Component
class UserMappers(
    @Value($$"${server.address}")
    private val serverAddress: String,
    @Value($$"${server.port}")
    private val serverPort: String,
    @Value($$"${server.protocol}")
    private val serverProtocol: String,
) {

    private val baseUrl = "$serverProtocol://$serverAddress:$serverPort/"

    fun UserEntity.toAuthResponse(
        uid: UUID,
        jwtToken: String,
        refreshToken: String,
        expiresIn: Long
    ) = AuthResponse(
        uid = uid,
        username = username,
        roles = roles,
        jwtToken = jwtToken,
        refreshToken = refreshToken,
        expiresIn = expiresIn
    )

    fun UserEntity.toResponse() = UserResponseModel(
        uid = requireNotNull(uid),
        username = username,
        email = email,
        name = name,
        photoUrl = baseUrl + photoUrl,
        createdAt = createdAt,
        roles = roles,
        phone = phone,
        bio = bio,
        addresses = addresses.map { it.toResponseModel() },
        paymentCartNumbers = paymentMethods.map { it.toResponseModel() }
    )

    fun AddressEntity.toResponseModel() = AddressResponseModel(
        id = id!!,
        name = name,
        address = address,
        isCurrentAddress = isCurrentAddress
    )

    fun PaymentMethodEntity.toResponseModel() = PaymentCardResponse(
        id = id!!,
        bank = bank,
        cardNumber = cardNumber
    )
}
