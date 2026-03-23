package com.foodback.dto.request.auth

/**
 * HTTP-Request to authenticate with Google OAuth2
 * @param idToken Google id token
 */
data class GoogleAuthRequest(
    val idToken: String
)
