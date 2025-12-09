package com.foodback.repository

import com.foodback.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Special interface to call SQL-query to database.
 */
interface ProductCategoriesRepository: JpaRepository<CategoryEntity, UUID> {
}