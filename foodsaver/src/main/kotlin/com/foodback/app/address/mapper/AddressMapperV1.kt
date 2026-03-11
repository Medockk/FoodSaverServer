package com.foodback.app.address.mapper

import com.foodback.app.address.dto.response.AddressResponseModelV1
import com.foodback.app.address.entity.AddressEntity
import org.springframework.stereotype.Component

@Component
class AddressMapperV1 {

    fun mapToResponse(address: AddressEntity) = with(address) {
        AddressResponseModelV1(
            id = id!!,
            name = name,
            address = this.address,
            isCurrentAddress = isCurrentAddress
        )
    }
}