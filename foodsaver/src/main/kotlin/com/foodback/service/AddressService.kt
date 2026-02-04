package com.foodback.service

import com.foodback.dto.request.address.AddAddressRequestModel
import com.foodback.dto.response.address.AddressResponseModel
import com.foodback.entity.AddressEntity
import com.foodback.exception.address.AddressException
import com.foodback.mappers.AddressMapper
import com.foodback.repository.AddressRepository
import com.foodback.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AddressService(
    private val addressRepository: AddressRepository,
    private val userRepository: UserRepository,

    private val addressMapper: AddressMapper
) {

    @Transactional
    fun addAddress(request: AddAddressRequestModel, uid: UUID): AddressResponseModel {
        val user = userRepository.findUserById(uid)

        if (request.isCurrentAddress) {
            addressRepository.disableAllAddresses(uid)
        }

        val entity = addressRepository.save(AddressEntity(
            user = user,
            name = request.name,
            address = request.address,
            isCurrentAddress = request.isCurrentAddress
        ))

        return addressMapper.mapToResponse(entity)
    }

    fun getAllAddresses(userUid: UUID): List<AddressResponseModel> {
        val addresses = addressRepository.findAllByUser_Uid(userUid)
        return addresses.map { addressMapper.mapToResponse(it) }
    }

    fun getCurrentAddress(userUid: UUID): AddressResponseModel? {
        val address = addressRepository.findByUser_UidAndIsCurrentAddress(userUid)
        return address?.let { addressMapper.mapToResponse(it) }
    }

    @Transactional
    fun setCurrentAddress(
        addressId: UUID,
        userUid: UUID
    ): AddressResponseModel {
        val addressEntity = addressRepository
            .findById(addressId)
            .orElseThrow { AddressException("Address not found!") }

        addressRepository.disableAllAddresses(uid = userUid)
        val newAddressValue = addressEntity.apply {
            isCurrentAddress = true
        }

        return addressMapper.mapToResponse(newAddressValue)
    }

    @Transactional
    fun deleteAddress(
        addressId: UUID,
        userUid: UUID
    ) {
        addressRepository.deleteByIdAndUser_Uid(addressId, userUid)
    }
}