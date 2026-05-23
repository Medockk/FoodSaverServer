package com.foodback.feature.ai.impl.controller

import com.foodback.feature.ai.api.service.AiIngredientService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.*

@RestController
@RequestMapping("/api/v1/ai/ingredients")
internal class AiIngredientsController(
    private val aiIngredientService: AiIngredientService
) {

    @GetMapping("/analyze", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun analyzeIngredients(@RequestParam productId: UUID): Flux<String> {
        val flux = aiIngredientService.streamIngredientClassificationForProduct(productId)

        return flux
    }

    @GetMapping("/analyze/json", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun analyzeIngredientsJson(@RequestParam productId: UUID): Flux<String> {
        val flux = aiIngredientService.streamIngredientClassificationForProductJson(productId)
        println("Flux в Контроллере $flux")
        return flux
    }
}