package com.foodback.demo.repository

import com.foodback.demo.entity.OrganizationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Special [JpaRepository] object, to working with table [OrganizationEntity]
 */
interface OrganizationRepository: JpaRepository<OrganizationEntity, UUID> {

}