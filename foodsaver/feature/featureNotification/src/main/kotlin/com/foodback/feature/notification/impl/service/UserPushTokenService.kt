package com.foodback.feature.notification.impl.service

import com.foodback.feature.notification.impl.entity.UserPushTokenEntity
import com.foodback.feature.notification.impl.repository.UserPushRepository
import com.foodback.feature.users.api.service.profile.ReadProfileService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
internal class UserPushTokenService(
    private val userPushRepository: UserPushRepository,
    private val profileService: ReadProfileService
) {

    @Transactional
    fun updateFirebaseToken(userId: UUID, firebaseToken: String) {
        // checking for existing user
        profileService.getProfileById(userId)

        val userTokenEntity = userPushRepository
            .findByFirebaseToken(firebaseToken)

        if (userTokenEntity != null) {
            // if token already saved by user
            // update userId (userA -> saveTokenForUserA. userA logout. userB -> the same token, but other user)

            userTokenEntity.userId = userId
        } else {
            val newEntity = UserPushTokenEntity(userId = userId)
            newEntity.firebaseToken = firebaseToken

            userPushRepository.save(newEntity)
        }
    }
}