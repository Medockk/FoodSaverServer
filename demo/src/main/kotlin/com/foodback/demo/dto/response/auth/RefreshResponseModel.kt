package com.foodback.demo.dto.response.auth

/**
 * Response from refresh request
 * @param jwtToken New JWT token
 * @param expiresIn Parameter to show, how long new JWT token will expire
 */
data class RefreshResponseModel(
    val jwtToken: String,
    val expiresIn: Long,
)
