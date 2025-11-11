package com.foodback.demo.repository

import com.foodback.demo.entity.ProductEntity
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

}