package com.foodback.feature.featureIngredients.impl.service

import com.foodback.feature.featureIngredients.api.dto.AddIngredientRequest
import com.foodback.feature.featureIngredients.api.dto.IngredientResponse
import com.foodback.feature.featureIngredients.api.service.ReadIngredientsService
import com.foodback.feature.featureIngredients.api.service.WriteIngredientsService
import com.foodback.feature.featureIngredients.impl.exception.IngredientNotFoundException
import com.foodback.feature.featureIngredients.impl.mapper.IngredientsMapper
import com.foodback.feature.featureIngredients.impl.repository.IngredientsRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
internal class IngredientsServiceImpl(
    private val ingredientsRepository: IngredientsRepository,
    private val ingredientsMapper: IngredientsMapper
): ReadIngredientsService, WriteIngredientsService {

    override fun getIngredientById(ingredientId: UUID): IngredientResponse {
        val ingredient = ingredientsRepository
            .findById(ingredientId)
            .orElseThrow { IngredientNotFoundException() }

        return ingredientsMapper.toResponse(ingredient)
    }

    override fun addIngredient(request: AddIngredientRequest): IngredientResponse {
        val entity = ingredientsMapper.toEntity(request)
        val ingredient = ingredientsRepository
            .save(entity)

        return ingredientsMapper.toResponse(ingredient)
    }
}