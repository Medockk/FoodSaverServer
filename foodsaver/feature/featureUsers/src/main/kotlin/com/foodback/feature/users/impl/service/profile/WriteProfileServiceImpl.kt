package com.foodback.feature.users.impl.service.profile

import com.foodback.core.coreMedia.api.service.MediaService
import com.foodback.feature.users.api.dto.proifle.CreateProfileRequest
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.api.dto.proifle.UpdateProfileRequest
import com.foodback.feature.users.api.service.profile.WriteProfileService
import com.foodback.feature.users.impl.exception.profile.ProfileNotFoundException
import com.foodback.feature.users.impl.mapper.ProfileMapper
import com.foodback.feature.users.impl.repository.AuthRepository
import com.foodback.feature.users.impl.repository.ProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
internal class WriteProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val profileMapper: ProfileMapper,
    private val mediaService: MediaService
): WriteProfileService {

    override fun createProfile(request: CreateProfileRequest): ProfileResponse {
        val entity = profileMapper.toEntity(request)
        val savedProfile = profileRepository.save(entity)
        val authEntity = authRepository.findByUid(savedProfile.userId)
            ?: throw ProfileNotFoundException()

        return profileMapper.toResponse(savedProfile, authEntity)
    }

    @Transactional
    override fun updateProfile(
        request: UpdateProfileRequest,
        avatarBytes: ByteArray?,
        avatarExtension: String?,
        userId: UUID
    ): ProfileResponse {
        val profile = profileRepository.findById(userId)
            .orElseThrow { ProfileNotFoundException() }
        val auth = authRepository.findByUid(userId)
            ?: throw ProfileNotFoundException()
        
        request.fullName?.let { profile.fullName = it }
        request.email?.let {
            // TODO mak sending email
            auth.email = it 
        }
        request.phone?.let { profile.phone = it }
        request.bio?.let { profile.bio = it }

        avatarBytes?.let {
            val relativeUri = mediaService.upload(
                bytes = avatarBytes,
                folder = "avatars/$userId",
                extension = avatarExtension ?: "png"
            )

            profile.imageUri = relativeUri
        }

        return profileMapper.toResponse(profile, auth)
    }
}