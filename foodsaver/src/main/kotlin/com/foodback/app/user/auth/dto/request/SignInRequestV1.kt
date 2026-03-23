package com.foodback.app.user.auth.dto.request

/**
 * Request to Authorize User
 * @param username Username
 * @param password Password of user
 * @param returnSecureToken Not required param. Should user get secure token or not
 */
data class SignInRequestV1(
    val username: String,
    val password: String,
    val returnSecureToken: Boolean
)