package com.foodback.feature.paymentMethod.api.dto

import java.time.Instant
import java.util.UUID

data class AddPaymentMethodRequest(
    val typeId: UUID,
    val cartHolderName: String,
    val cardNumber: String,
    val expiresDate: Instant,
    val cvc: String
)
