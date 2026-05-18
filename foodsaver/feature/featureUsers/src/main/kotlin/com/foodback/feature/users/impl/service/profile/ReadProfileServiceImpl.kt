package com.foodback.feature.users.impl.service.profile

import com.foodback.feature.address.api.dto.AddressResponse
import com.foodback.feature.address.api.service.AddressService
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.api.service.auth.ReadAuthService
import com.foodback.feature.users.api.service.profile.ReadProfileService
import com.foodback.feature.users.impl.exception.profile.ProfileNotFoundException
import com.foodback.feature.users.impl.mapper.ProfileMapper
import com.foodback.feature.users.impl.repository.AuthRepository
import com.foodback.feature.users.impl.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class ReadProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val profileMapper: ProfileMapper,
    private val addressService: AddressService
): ReadProfileService {

    override fun getProfileById(profileId: UUID): ProfileResponse {
        val profile = profileRepository.findById(profileId)
            .orElseThrow { ProfileNotFoundException() }
        val auth = authRepository
            .findByUid(profile.userId)
            ?: throw ProfileNotFoundException()

        return profileMapper.toResponse(profile, auth)
    }

    override fun getProfileAddresses(profileId: UUID): List<AddressResponse> {
        val profile = profileRepository
            .findById(profileId)
            .orElseThrow { ProfileNotFoundException() }
        val addressIds = profile.addressIds
        val addresses = addressService.getAddressesByIds(addressIds)
        return addresses
    }

    override fun getProfileAddress(profileId: UUID): AddressResponse? {
        val profile = profileRepository
            .findById(profileId)
            .orElseThrow { ProfileNotFoundException() }

        val currentAddressId = profile.currentAddressId ?: return null
        val address = addressService
            .getAddressById(currentAddressId)

        return address
    }
}