package com.foodback.feature.paymentMethod.api.dto

import java.time.Instant
import java.util.UUID

data class PaymentMethodResponse(
    val id: UUID,
    val type: PaymentMethodTypeResponse,
    val holderName: String?,
    val lastFourSymbols: String?,
    val isSelected: Boolean,
    val expiresDate: Instant?,
    val addedAt: Instant,
)
