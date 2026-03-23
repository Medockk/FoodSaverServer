package com.foodback.app.address.dto.request

data class AddAddressRequestModelV1(
    val name: String,
    val address: String,
    val isCurrentAddress: Boolean
)