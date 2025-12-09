package com.foodback.repository

import com.foodback.entity.OffersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OffersRepository: JpaRepository<OffersEntity, UUID> {


}