package com.foodback.feature.users.api.dto.proifle

data class UpdateProfileRequest(
    val fullName: String?,
    val phone: String?,
    val bio: String?,
    val email: String?
)
