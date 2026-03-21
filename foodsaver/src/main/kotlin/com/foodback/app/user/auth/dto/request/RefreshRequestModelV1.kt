package com.foodback.app.user.auth.dto.request

/**
 * Request to refresh JWT Token
 * @param refreshToken Refresh token to get new JWT token
 * @param accessToken Access token if it exists
 */
data class RefreshRequestModelV1(
    val refreshToken: String,
    val accessToken: String?
)
