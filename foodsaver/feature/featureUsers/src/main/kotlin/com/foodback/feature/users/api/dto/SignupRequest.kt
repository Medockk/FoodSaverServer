package com.foodback.feature.users.api.dto

data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String
)
