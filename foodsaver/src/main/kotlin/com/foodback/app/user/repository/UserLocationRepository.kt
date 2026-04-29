package com.foodback.app.user.repository

import com.foodback.app.user.entity.UserLocationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserLocationRepository: JpaRepository<UserLocationEntity, UUID> {

    fun findByUserUid(uid: UUID): Optional<UserLocationEntity>
}