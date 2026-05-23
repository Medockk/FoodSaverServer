package com.foodback.feature.featureIngredients.impl.controller

import com.foodback.feature.featureIngredients.api.dto.AddIngredientRequest
import com.foodback.feature.featureIngredients.api.dto.IngredientResponse
import com.foodback.feature.featureIngredients.api.service.ReadIngredientsService
import com.foodback.feature.featureIngredients.api.service.WriteIngredientsService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/ingredients")
internal class IngredientsController(
    private val readIngredientsService: ReadIngredientsService,
    private val writeIngredientsService: WriteIngredientsService
) {

    @GetMapping("/id")
    fun getIngredientById(
        @RequestParam
        ingredientId: UUID
    ): ResponseEntity<IngredientResponse> {
        val ingredient = readIngredientsService
            .getIngredientById(ingredientId)

        return ResponseEntity.ok(ingredient)
    }

    @GetMapping("/ids")
    fun getIngredientsByIds(
        @RequestParam
        ids: List<UUID>
    ): ResponseEntity<List<IngredientResponse>> {
        val ingredients = readIngredientsService.getIngredientsByIds(ids)

        return if (ingredients.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(ingredients)
    }

    @GetMapping("/all")
    fun getAllIngredients(): ResponseEntity<List<IngredientResponse>> {
        val ingredients = readIngredientsService.getAllIngredients()

        return if (ingredients.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(ingredients)
    }

    @PreAuthorize("hasAuthority('ADD_CATEGORY')")
    @PostMapping("/add")
    fun addIngredient(
        @RequestPart
        request: AddIngredientRequest
    ): ResponseEntity<IngredientResponse> {
        val response = writeIngredientsService
            .addIngredient(request)

        return ResponseEntity.ok(response)
    }
}