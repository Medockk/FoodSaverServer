package com.foodback.feature.address.impl.repository

import com.foodback.feature.address.impl.entity.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface AddressRepository: JpaRepository<AddressEntity, UUID> {
}