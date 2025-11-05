package com.foodback.demo.dto.response.auth

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response from refresh request
 * @param jwtToken New JWT token
 * @param expiresIn Parameter to show, how long new JWT token will expire
 */
data class RefreshResponseModel(
    @JsonProperty("id_token") val jwtToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
)
