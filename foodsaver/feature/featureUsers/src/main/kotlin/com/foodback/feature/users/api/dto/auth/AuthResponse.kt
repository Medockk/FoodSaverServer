package com.foodback.feature.users.api.dto.auth

import java.util.*

data class AuthResponse(
    val uid: UUID,
    val accessToken: String,
    val refreshToken: String,
    val permissions: List<String>
)
