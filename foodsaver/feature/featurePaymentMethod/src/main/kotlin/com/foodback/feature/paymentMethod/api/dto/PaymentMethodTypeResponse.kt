package com.foodback.feature.paymentMethod.api.dto

import java.util.UUID

data class PaymentMethodTypeResponse(
    val id: UUID,
    val name: String,
    val iconUri: String?
)
