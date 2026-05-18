package com.foodback.feature.users.api.service.profile

import com.foodback.feature.users.api.dto.proifle.CreateProfileRequest
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.api.dto.proifle.UpdateProfileRequest
import java.util.UUID

interface WriteProfileService {

    fun createProfile(request: CreateProfileRequest): ProfileResponse
    fun updateProfile(
        request: UpdateProfileRequest,
        avatarBytes: ByteArray?,
        avatarExtension: String?,
        userId: UUID
    ): ProfileResponse
}