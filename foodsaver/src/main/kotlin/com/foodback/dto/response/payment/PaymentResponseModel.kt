package com.foodback.dto.response.payment

import com.fasterxml.jackson.annotation.JsonProperty

data class PaymentResponseModel(
    val id: String,
    val bank: String,
    val cardNumber: String,
    @JsonProperty("isSelected")
    val isSelected: Boolean
)
