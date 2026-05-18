package com.foodback.feature.address.impl.service

import com.foodback.feature.address.api.dto.AddAddressRequest
import com.foodback.feature.address.api.dto.AddressResponse
import com.foodback.feature.address.api.service.AddressService
import com.foodback.feature.address.impl.exception.AddressNotFoundException
import com.foodback.feature.address.impl.mapper.AddressMapper
import com.foodback.feature.address.impl.repository.AddressRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
internal class AddressServiceImpl(
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper
): AddressService {

    override fun addAddress(request: AddAddressRequest): AddressResponse {
        val entity = addressMapper.toEntity(request)
        val savedAddress = addressRepository.save(entity)

        return addressMapper.toResponse(savedAddress)
    }

    override fun getAddressById(id: UUID): AddressResponse {
        val entity = addressRepository
            .findById(id)
            .orElseThrow { AddressNotFoundException() }

        return addressMapper.toResponse(entity)
    }

    override fun getAddressesByIds(ids: List<UUID>): List<AddressResponse> {
        val addresses = addressRepository.findAllById(ids)

        return addresses.map {
            addressMapper.toResponse(it)
        }
    }

    @Transactional
    override fun deleteAddressById(id: UUID) {
        val entity = addressRepository
            .findById(id)
            .getOrNull()

        entity?.isDeleted = true
    }
}