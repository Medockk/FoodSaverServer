package com.foodback.dto.request.auth

/**
 * Request to Authorize User
 * @param username Username
 * @param password Password of user
 * @param returnSecureToken Not required param. Should user get secure token or not
 */
data class SignInRequest(
    val username: String,
    val password: String,
    val returnSecureToken: Boolean
)