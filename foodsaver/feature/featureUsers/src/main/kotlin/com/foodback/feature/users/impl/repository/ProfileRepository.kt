package com.foodback.feature.users.impl.repository

import com.foodback.feature.users.impl.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface ProfileRepository: JpaRepository<UserEntity, UUID> {
}