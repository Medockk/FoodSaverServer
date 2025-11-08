package com.foodback.demo.mappers

import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.dto.response.user.UserResponseModel
import com.foodback.demo.entity.User.UserEntity
import java.util.*

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