package com.foodback.demo.repository

import com.foodback.demo.entity.CartEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * interface to access with table 'cart' in database
 */
interface CartRepository : JpaRepository<CartEntity, UUID> {

    /**
     * Method to find [CartEntity] by [uid]
     * @return An [Optional] of [CartEntity]
     */
    fun findByUid(uid: UUID): Optional<CartEntity>

    /**
     * Method to delete all [CartEntity] by [uid]
     */
    fun deleteAllByUid(uid: UUID)
}