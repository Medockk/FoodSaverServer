package com.foodback.demo.repository

import com.foodback.demo.entity.CartEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CartRepository : JpaRepository<CartEntity, UUID> {

    fun findByUid(uid: UUID): Optional<CartEntity>
    fun deleteAllByUid(uid: UUID)
}