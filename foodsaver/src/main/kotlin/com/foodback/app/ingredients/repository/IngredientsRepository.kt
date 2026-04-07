package com.foodback.app.ingredients.repository

import com.foodback.app.ingredients.entity.IngredientsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface IngredientsRepository: JpaRepository<IngredientsEntity, UUID> {

    fun findByProductId(productId: UUID): Optional<IngredientsEntity>
}