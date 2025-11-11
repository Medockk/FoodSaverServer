package com.foodback.demo.dto.request.auth

import jakarta.validation.constraints.Email

/**
 * Request to reset password with [email]
 * @param email User email to send RESET-TOKEN
 */
data class ResetPasswordRequest(
    @field:Email
    val email: String
)
