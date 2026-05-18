package com.foodback.feature.users.impl.repository

import com.foodback.feature.users.impl.entity.AuthEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface AuthRepository: JpaRepository<AuthEntity, UUID> {
    fun findByUsername(username: String): AuthEntity?
    fun findByEmail(email: String): AuthEntity?

    fun findByGoogleId(googleId: String): AuthEntity?

    fun findByUid(uid: UUID): AuthEntity?
}