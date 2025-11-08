package com.foodback.demo.dto.response.user

import java.time.Instant
import java.util.UUID

data class UserResponseModel(
    val uid: UUID,
    val username: String,
    val email: String?,
    val name: String?,
    val photoUrl: String?,
    val createdAt: Instant,
    val roles: List<String>
)
