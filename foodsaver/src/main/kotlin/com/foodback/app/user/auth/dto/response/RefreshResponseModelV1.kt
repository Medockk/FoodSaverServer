package com.foodback.app.user.auth.dto.response

/**
 * Response from refresh request
 * @param jwtToken New JWT token
 * @param expiresIn Parameter to show, how long new JWT token will expire
 */
data class RefreshResponseModelV1(
    val jwtToken: String,
    val expiresIn: Long,
)
