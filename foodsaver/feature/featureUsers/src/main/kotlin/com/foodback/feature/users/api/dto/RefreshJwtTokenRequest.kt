package com.foodback.feature.users.api.dto

data class RefreshJwtTokenRequest(
    val accessToken: String,
    val refreshToken: String
)
