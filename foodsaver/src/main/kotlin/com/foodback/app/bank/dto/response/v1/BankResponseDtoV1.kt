package com.foodback.app.bank.dto.response.v1

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class BankResponseDtoV1(
    val id: UUID,
    val cardNumber: String,
    val balance: Double,
    @JsonProperty("isSelected")
    val isSelected: Boolean
)
