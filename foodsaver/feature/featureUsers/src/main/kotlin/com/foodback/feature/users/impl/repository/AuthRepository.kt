package com.foodback.feature.users.impl.repository

import com.foodback.feature.users.impl.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface AuthRepository: JpaRepository<UserEntity, UUID> {
    fun findByUsername(username: String): UserEntity?
    fun findByEmail(email: String): UserEntity?

    fun findByGoogleId(googleId: String): UserEntity?
}