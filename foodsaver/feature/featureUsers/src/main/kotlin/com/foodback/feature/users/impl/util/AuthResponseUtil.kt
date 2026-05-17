package com.foodback.feature.users.impl.util

import com.foodback.core.coreSecurity.api.service.JwtService
import com.foodback.feature.users.api.dto.auth.AuthResponse
import com.foodback.feature.users.impl.entity.UserEntity
import org.springframework.stereotype.Service

@Service
internal class AuthResponseUtil(
    private val jwtService: JwtService
) {

    fun buildAuthResponse(user: UserEntity): AuthResponse {
        val accessToken = jwtService.generateAccessToken(user.username)
        val refreshToken = jwtService.generateRefreshToken(user.username)
        val permissions = user.roles.flatMap { it.permissions }.map { it.name }

        return AuthResponse(
            uid = user.uid!!,
            accessToken = accessToken,
            refreshToken = refreshToken,
            permissions = permissions
        )
    }
}