package com.foodback.app.address.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class AddressResponseModelV1(
    val id: UUID,
    val name: String,
    val address: String,
    @JsonProperty("isCurrentAddress")
    val isCurrentAddress: Boolean
)