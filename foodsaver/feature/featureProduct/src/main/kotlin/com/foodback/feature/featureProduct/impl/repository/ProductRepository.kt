package com.foodback.feature.featureProduct.impl.repository

import com.foodback.feature.featureProduct.impl.entity.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.util.*

internal interface ProductRepository :
    JpaRepository<ProductEntity, UUID>,
    JpaSpecificationExecutor<ProductEntity> {

    fun findAllByIsDeletedFalse(pageable: Pageable): Page<ProductEntity>
    fun findAllByRestaurantIdAndIsDeletedFalse(restaurantId: UUID, pageable: Pageable): Page<ProductEntity>

    fun findAllByCategoryIdsIn(categoryIds: List<UUID>, pageable: Pageable): Page<ProductEntity>

    @Modifying
    @Query(
        """
        UPDATE ProductEntity
        SET isDeleted = true
        WHERE expiresAt < :cutOffTime
    """
    )
    fun markExpiresProductsDeleted(cutOffTime: Instant): Long

    fun findAllByIsSuggestedTrue(): List<ProductEntity>
}