package com.foodback.app.ingredients.controller

import com.foodback.app.ingredients.dto.response.IngredientResponseModel
import com.foodback.app.ingredients.mappers.IngredientMapper
import com.foodback.app.ingredients.service.IngredientsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/ingredients")
class IngredientsController(
    private val ingredientsService: IngredientsService,
    private val ingredientMapper: IngredientMapper
) {

    @GetMapping
    fun getIngredientsByProductId(@RequestParam productId: UUID): ResponseEntity<IngredientResponseModel> {
        val ingredients = ingredientsService.getIngredientsByProductId(productId)
            ?: return ResponseEntity.noContent().build()
        val body = ingredientMapper.mapToResponse(ingredients)

        return ResponseEntity
            .ok(body)
    }
}