package com.foodback.feature.notification.impl.repository

import com.foodback.feature.notification.impl.entity.UserPushTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface UserPushRepository: JpaRepository<UserPushTokenEntity, UUID> {

    fun findByUserId(userId: UUID): UserPushTokenEntity?
    fun findByFirebaseToken(token: String): UserPushTokenEntity?
}