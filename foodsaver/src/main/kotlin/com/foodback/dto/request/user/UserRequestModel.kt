package com.foodback.dto.request.user

import com.foodback.utils.validation.NotBlankOrNull
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
    @field:NotBlankOrNull(message = "Name must be not blank or null")
    val name: String?,
    val photoUrl: String?
)

