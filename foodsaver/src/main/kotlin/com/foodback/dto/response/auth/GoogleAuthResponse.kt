package com.foodback.dto.response.auth

/**
 * HTTP-Response after verifying google id token
 * @param idToken Google id token
 * @param email User email
 * @param name User full-name
 * @param picture URL to user avatar
 * @param isEmailVerified True, if email verified
 */
data class GoogleAuthResponse(
    val idToken: String,
    val email: String,
    val name: String,
    val picture: String,
    val isEmailVerified: Boolean = false
)
