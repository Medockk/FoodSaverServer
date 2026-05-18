package com.foodback.feature.users.impl.repository

import com.foodback.feature.users.impl.entity.ProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

internal interface ProfileRepository: JpaRepository<ProfileEntity, UUID> {


}