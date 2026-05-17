package com.foodback.feature.category.impl.repository

import com.foodback.feature.category.impl.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface CategoryRepository: JpaRepository<CategoryEntity, UUID> {

    fun findAllByNameContainingIgnoreCase(name: String): List<CategoryEntity>
}