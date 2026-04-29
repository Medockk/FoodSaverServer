package com.foodback.app.user.service

import com.foodback.app.user.dto.request.UpdateUserLocationRequestModel
import com.foodback.app.user.repository.UserLocationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class UserLocationService(
    private val userLocationRepository: UserLocationRepository
) {

    @Transactional
    fun updateLocation(
        uid: UUID,
        request: UpdateUserLocationRequestModel
    ) {
        val user = userLocationRepository
            .findByUserUid(uid)
            .getOrNull() ?: return

        user.latitude = request.latitude
        user.longitude = request.longitude
    }
}