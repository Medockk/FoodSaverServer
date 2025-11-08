package com.foodback.demo.dto.request.auth

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email

/**
 * Request to Register new user
 * @param username Username
 * @param password Password of user
 * @param displayName Not required param. User name
 * @param email Email of user
 * @param returnSecureToken Not required param. Should user get secure token or not
 */
data class SignUpRequest(
    val username: String,
    val password: String,
    @JsonProperty(required = false)
    val displayName: String? = null,
    @JsonProperty(required = false)
    @field:Email(message = "Invalid email format")
    val email: String? = null,
    val returnSecureToken: Boolean = true,
)
