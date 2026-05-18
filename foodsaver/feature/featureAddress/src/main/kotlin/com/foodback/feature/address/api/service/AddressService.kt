package com.foodback.feature.address.api.service

import com.foodback.feature.address.api.dto.AddAddressRequest
import com.foodback.feature.address.api.dto.AddressResponse
import java.util.UUID

interface AddressService {

    fun addAddress(request: AddAddressRequest): AddressResponse
    fun getAddressById(id: UUID): AddressResponse
    fun getAddressesByIds(ids: List<UUID>): List<AddressResponse>
    fun deleteAddressById(id: UUID)
}