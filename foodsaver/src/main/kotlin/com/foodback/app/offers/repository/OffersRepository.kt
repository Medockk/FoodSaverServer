package com.foodback.app.offers.repository

import com.foodback.app.offers.entity.OffersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OffersRepository: JpaRepository<OffersEntity, UUID> {


}