package com.foodback.demo.dto.request.user

import jakarta.validation.constraints.Email

data class UserRequestModel(
    @field:Email(message = "Invalid email type")
    val email: String?,
    val name: String?,
    val photoUrl: String?
)

