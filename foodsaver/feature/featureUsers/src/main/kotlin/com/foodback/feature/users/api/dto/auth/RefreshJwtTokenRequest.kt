package com.foodback.feature.users.api.dto.auth

data class RefreshJwtTokenRequest(
    val accessToken: String,
    val refreshToken: String
)
