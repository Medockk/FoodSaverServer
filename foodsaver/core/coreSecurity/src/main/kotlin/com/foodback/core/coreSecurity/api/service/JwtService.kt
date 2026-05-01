package com.foodback.core.coreSecurity.api.service

interface JwtService {

    fun generateAccessToken(username: String): String
    fun generateRefreshToken(username: String): String
    fun getUsername(token: String): String
    fun validateToken(token: String): Boolean

    companion object {
        const val JWT_COOKIE_NAME = "jwt"
    }
}