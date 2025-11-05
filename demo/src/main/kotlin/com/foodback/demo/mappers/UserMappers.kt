package com.foodback.demo.mappers

import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.entity.User.UserEntity
import java.util.*

fun UserEntity.toResponse(
    uid: UUID,
    jwtToken: String,
    refreshToken: String,
    expiresIn: Long
) =
    AuthResponse(
        uid = uid,
        username = username,
        roles = roles,
        jwtToken = jwtToken,
        refreshToken = refreshToken,
        expiresIn = expiresIn
    )