package com.foodback.demo.dto.request.user

import jakarta.validation.constraints.Email

/**
 * Request to update user data
 * @param email New user email
 * @param name New name of user
 * @param photoUrl URL to new avatar
 */
data class UserRequestModel(
    @field:Email(message = "Invalid email type")
    val email: String?,
    val name: String?,
    val photoUrl: String?
)

