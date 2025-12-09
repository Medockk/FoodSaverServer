package com.foodback.mappers

import com.foodback.dto.response.address.AddressResponseModel
import com.foodback.entity.AddressEntity
import org.springframework.stereotype.Component

@Component
class AddressMapper {

    fun mapToResponse(address: AddressEntity) = with(address) {
        AddressResponseModel(
            id = id!!,
            name = name,
            address = this.address,
            isCurrentAddress = isCurrentAddress
        )
    }
}