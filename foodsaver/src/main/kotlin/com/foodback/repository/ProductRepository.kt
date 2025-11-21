package com.foodback.repository

import com.foodback.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

/**
 * interface to access with table 'products' in database
 */
interface ProductRepository : JpaRepository<ProductEntity, UUID> {

    /**
     * Method to delete [ProductEntity] if product already expired
     * @return count of deleted products
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductEntity product WHERE product.expiresAt < :cutOffTime")
    fun deleteProductAfterExpiresAt(cutOffTime: Instant): Int

    /**
     * Method to find all [ProductEntity], containing special [title]
     * @param title Product title
     * @return A [List] of [ProductEntity] with special [title]
     */
    fun findAllByTitleContainingIgnoreCase(title: String): List<ProductEntity>

    /**
     * Method to find all [ProductEntity] with special [categoryId]. This method with strange named because
     * Spring Data Jpa generate SQL-queries by method names.
     * @param categoryId Identifier of category
     * @return A [List] of [ProductEntity] with special [categoryId]
     */
    fun findAllByCategories_Id(categoryId: UUID): List<ProductEntity>
}