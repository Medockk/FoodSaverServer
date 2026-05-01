package com.foodback.base.module.impl.repository

import com.foodback.base.module.impl.entity.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface CompanyRepository: JpaRepository<CompanyEntity, UUID> {
}