package com.foodback.dto.request.auth

/**
 * Request to refresh JWT Token
 * @param refreshToken Refresh token to get new JWT token
 * @param accessToken Access token if it exists
 */
data class RefreshRequestModel(
    val refreshToken: String,
    val accessToken: String?
)
