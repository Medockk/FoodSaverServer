package com.foodback.feature.users.api.dto.auth

data class LoginRequest(
    val email: String,
    val password: String
)
