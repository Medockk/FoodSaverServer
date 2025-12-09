package com.foodback.dto.request.payment

data class PaymentRequestModel(
    val bank: String,
    val cardNumber: String,
    val isSelected: Boolean = false
)
