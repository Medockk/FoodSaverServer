package com.foodback.feature.users.api.dto.auth

data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String
)
