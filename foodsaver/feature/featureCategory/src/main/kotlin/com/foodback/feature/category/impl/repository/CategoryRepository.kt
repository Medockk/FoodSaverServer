package com.foodback.feature.category.impl.repository

import com.foodback.feature.category.impl.entity.CategoryEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface CategoryRepository: JpaRepository<CategoryEntity, UUID> {

    fun findAllByNameContainingIgnoreCaseAndIsDeletedFalse(name: String): List<CategoryEntity>
    fun findByNameContainingIgnoreCase(name: String): CategoryEntity?

    fun findAllByIsDeletedFalse(): List<CategoryEntity>
    fun findAllByIsDeletedFalse(pageable: Pageable): Page<CategoryEntity>
}