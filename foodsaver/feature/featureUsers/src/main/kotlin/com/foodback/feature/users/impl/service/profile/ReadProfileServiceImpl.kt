package com.foodback.feature.users.impl.service.profile

import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.api.service.profile.ReadProfileService
import com.foodback.feature.users.impl.exception.profile.ProfileNotFoundException
import com.foodback.feature.users.impl.mapper.ProfileMapper
import com.foodback.feature.users.impl.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class ReadProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val profileMapper: ProfileMapper
): ReadProfileService {

    override fun getProfileById(profileId: UUID): ProfileResponse {
        val profile = profileRepository.findById(profileId)
            .orElseThrow { ProfileNotFoundException() }

        return profileMapper.toResponse(profile)
    }
}