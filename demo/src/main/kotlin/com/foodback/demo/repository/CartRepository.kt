package com.foodback.demo.repository

import com.foodback.demo.entity.CartEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface CartRepository: JpaRepository<CartEntity, UUID> {

    fun findByUid(uid: String): Optional<CartEntity>
}