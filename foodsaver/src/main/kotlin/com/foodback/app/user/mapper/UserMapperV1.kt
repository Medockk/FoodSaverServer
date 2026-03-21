package com.foodback.app.user.mapper

import com.foodback.app.address.dto.response.AddressResponseModelV1
import com.foodback.app.address.entity.AddressEntity
import com.foodback.app.user.auth.dto.response.AuthResponseV1
import com.foodback.app.user.dto.response.UserResponseModelV1
import com.foodback.app.user.entity.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UserMapperV1(
    @Value($$"${server.address}")
    private val serverAddress: String,
    @Value($$"${server.port}")
    private val serverPort: String,
    @Value($$"${server.protocol}")
    private val serverProtocol: String,

    @Value($$"${jwt.expiration.ms}")
    private val jwtExpirationMs: Long
) {

    private val baseUrl = "$serverProtocol://$serverAddress:$serverPort/"

    fun toAuthResponse(
        userEntity: UserEntity,
        jwtToken: String,
        refreshToken: String
    ) = with(userEntity) {
        AuthResponseV1(
            uid = uid!!,
            username = username,
            roles = roles,
            jwtToken = jwtToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpirationMs
        )
    }

    fun mapToResponse(userEntity: UserEntity) = with(userEntity) {
        UserResponseModelV1(
            uid = uid!!,
            username = username,
            email = email,
            name = name,
            photoUrl = photoUrl?.let {
                if (it.startsWith("http")) {
                    return@let it
                } else if (it.startsWith("/")) {
                    return@let baseUrl + it.drop(1)
                } else {
                    return@let baseUrl + it
                }
            },
            createdAt = createdAt,
            roles = roles,
            phone = phone,
            bio = bio
        )
    }

    fun AddressEntity.toResponseModel() = AddressResponseModelV1(
        id = id!!,
        name = name,
        address = address,
        isCurrentAddress = isCurrentAddress
    )
}