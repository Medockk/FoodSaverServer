package com.foodback.dto.request.address

data class AddAddressRequestModel(
    val name: String,
    val address: String,
    val isCurrentAddress: Boolean
)
