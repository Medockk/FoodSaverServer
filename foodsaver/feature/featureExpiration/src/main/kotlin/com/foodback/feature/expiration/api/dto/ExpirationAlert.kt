package com.foodback.feature.expiration.api.dto

import java.util.UUID

data class ExpirationAlert(
    val token: String,
    val productName: String,
    val productId: UUID
)