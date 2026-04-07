package com.foodback.app.ingredients.service

import com.foodback.app.ingredients.entity.IngredientsEntity
import com.foodback.app.ingredients.repository.IngredientsRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class IngredientsService(
    private val ingredientsRepository: IngredientsRepository
) {

    fun getIngredientsByProductId(productId: UUID): IngredientsEntity? {
        val ingredients = ingredientsRepository
            .findByProductId(productId)
            .getOrNull()

        return ingredients
    }
}