package com.foodback.feature.users.api.dto.proifle

import java.util.*

data class CreateProfileRequest(
    val userId: UUID,
    var fullName: String,
    var phone: String?,
    var bio: String?,
    var imageUri: String?,
)
