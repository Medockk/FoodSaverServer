package com.foodback.app.user.auth.dto.request

/**
 * HTTP-Request to authenticate with Google OAuth2
 * @param idToken Google id token
 */
data class GoogleAuthRequestV1(
    val idToken: String
)
