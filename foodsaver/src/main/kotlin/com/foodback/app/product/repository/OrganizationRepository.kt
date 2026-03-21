package com.foodback.app.product.repository

import com.foodback.app.product.entity.OrganizationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Special [JpaRepository] object, to working with table [OrganizationEntity]
 */
interface OrganizationRepository: JpaRepository<OrganizationEntity, UUID> {

}