package com.foodback.mappers

import com.foodback.dto.response.auth.AuthResponse
import com.foodback.dto.response.user.UserResponseModel
import com.foodback.entity.User.UserEntity
import java.util.*

// Methods to convert Request to entity and vice versa

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
    photoUrl = photoUrl,
    createdAt = createdAt,
    roles = roles
)