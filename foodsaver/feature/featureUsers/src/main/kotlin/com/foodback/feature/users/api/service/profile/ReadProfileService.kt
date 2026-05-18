package com.foodback.feature.users.api.service.profile

import com.foodback.feature.address.api.dto.AddressResponse
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import java.util.*

interface ReadProfileService {

    fun getProfileById(profileId: UUID): ProfileResponse
    fun getProfileAddresses(profileId: UUID): List<AddressResponse>

    fun getProfileAddress(profileId: UUID): AddressResponse?
}