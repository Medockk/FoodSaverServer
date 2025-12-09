package com.foodback.dto.request.user

import com.foodback.utils.validation.NotBlankOrNull
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Request to update user data
 * @param email New user email
 * @param name New name of user
 * @param photoUrl URL to new avatar
 */
data class UserRequestModel(
    @field:Email(message = "Invalid email type")
    val email: String,
    @field:NotBlank(message = "FullName must be not blank!")
    val fullName: String,
    val phone: String?,
    val bio: String?,
    val photoUrl: String?
)

