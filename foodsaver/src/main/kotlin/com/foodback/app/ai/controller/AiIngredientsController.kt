package com.foodback.app.ai.controller

import com.foodback.app.ai.service.AiServiceAnalyst
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ai/ingredients")
class AiIngredientsController(
    private val aiServiceAnalyst: AiServiceAnalyst
) {

    @GetMapping
    fun analyzeIngredients(@RequestBody ingredients: String): ResponseEntity<String> {
        val analyzeResponse = aiServiceAnalyst.analyzeIngredients(ingredients)
        return ResponseEntity
            .ok()
            .body(analyzeResponse)
    }
}