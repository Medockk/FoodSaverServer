package com.foodback.repository

import com.foodback.entity.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    fun findAllByTitleContainingIgnoreCase(title: String, pageable: Pageable): Page<ProductEntity>

    /**
     * Method to find some [ProductEntity] with special [categoryIds]. This method with strange named because
     * Spring Data Jpa generate SQL-queries by method names.
     * @param categoryIds Identifiers of categories
     * @param pageable This parameter set LIMIT and OFFSET to SQL-query
     * @return A [List] of [ProductEntity] with special [categoryIds]
     */
    fun findAllByCategories_IdIn(categoryIds: List<UUID>, pageable: Pageable): Page<ProductEntity>

    /**
     * Method to get some [ProductEntity] with special [organizationId]
     * @param organizationId The Identifier of current organization
     * @param pageable This parameter set LIMIT and OFFSET to SQL-query
     * @return A [List] of [ProductEntity] with special [organizationId]
     */
    fun findAllByOrganization_Id(organizationId: UUID, pageable: Pageable): Page<ProductEntity>

    fun findAllByTitleContainingIgnoreCaseAndCategories_IdIn(title: String, categoryIds: List<UUID>, pageable: Pageable): Page<ProductEntity>

    /**
     * Method to get some [ProductEntity] with special [organizationId] and special [categoryId]
     * @param organizationId The Identifier of current organization
     * @param categoryId The identifier of products category
     * @param pageable This parameter set LIMIT and OFFSET to SQL-query
     * @return A [List] of [ProductEntity] with special [organizationId] and [categoryId]
     */
    fun findAllByOrganization_IdAndCategories_Id(organizationId: UUID, categoryId: UUID, pageable: Pageable): Page<ProductEntity>
}