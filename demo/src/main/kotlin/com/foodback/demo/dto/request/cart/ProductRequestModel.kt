package com.foodback.demo.dto.request.cart

import java.time.Instant


data class ProductRequestModel(
    val title: String,
    val description: String,
    val cost: Float,
    val organization: String,

    val expiresAt: Instant? = Instant.now(),
    val addedAt: Instant? = Instant.now(),

)
