package com.foodback.dto.response.user

import java.util.UUID

data class PaymentCardResponse(
    val id: UUID,
    val bank: String,
    val cardNumber: String
)
