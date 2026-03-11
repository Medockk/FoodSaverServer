package com.foodback.app.address.service

import com.foodback.app.address.dto.request.AddAddressRequestModelV1
import com.foodback.app.address.entity.AddressEntity
import com.foodback.app.address.repository.AddressRepository
import com.foodback.app.user.repository.UserRepository
import com.foodback.exception.address.AddressException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AddressService(
    private val addressRepository: AddressRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun addAddress(request: AddAddressRequestModelV1, uid: UUID): AddressEntity {
        val user = userRepository.findUserById(uid)

        if (request.isCurrentAddress) {
            addressRepository.disableAllAddresses(uid)
        }

        val entity = addressRepository.save(
            AddressEntity(
                user = user,
                name = request.name,
                address = request.address,
                isCurrentAddress = request.isCurrentAddress
            )
        )

        return entity
    }

    fun getAllAddresses(userUid: UUID): List<AddressEntity> {
        val addresses = addressRepository.findAllByUser_Uid(userUid)
        return addresses
    }

    fun getCurrentAddress(userUid: UUID): AddressEntity? {
        val address = addressRepository.findByUser_UidAndIsCurrentAddress(userUid)
        return address
    }

    @Transactional
    fun setCurrentAddress(
        addressId: UUID,
        userUid: UUID
    ): AddressEntity {
        val addressEntity = addressRepository
            .findById(addressId)
            .orElseThrow { AddressException("Address not found!") }

        addressRepository.disableAllAddresses(uid = userUid)
        val newAddressValue = addressEntity.apply {
            isCurrentAddress = true
        }

        return newAddressValue
    }

    @Transactional
    fun deleteAddress(
        addressId: UUID,
        userUid: UUID
    ) {
        addressRepository.deleteByIdAndUser_Uid(addressId, userUid)
    }
}