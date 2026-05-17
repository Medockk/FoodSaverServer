package com.foodback.feature.featureIngredients.impl.repository

import com.foodback.feature.featureIngredients.impl.entity.IngredientsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface IngredientsRepository: JpaRepository<IngredientsEntity, UUID> {
}