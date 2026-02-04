package com.foodback.dto.response.address

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class AddressResponseModel(
    val id: UUID,
    val name: String,
    val address: String,
    @JsonProperty("isCurrentAddress")
    val isCurrentAddress: Boolean
)