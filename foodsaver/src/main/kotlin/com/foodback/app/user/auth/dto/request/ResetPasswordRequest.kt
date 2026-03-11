package com.foodback.app.user.auth.dto.request

import jakarta.validation.constraints.Email

/**
 * Request to reset password with [email]
 * @param email User email to send RESET-TOKEN
 */
data class ResetPasswordRequestV1(
    @field:Email
    val email: String
)

data class NewPasswordRequestV1(
    val password: String,
    val confirmPassword: String,
)
