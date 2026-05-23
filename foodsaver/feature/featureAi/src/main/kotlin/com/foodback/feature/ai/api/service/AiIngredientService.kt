package com.foodback.feature.ai.api.service

import reactor.core.publisher.Flux
import java.util.UUID

interface AiIngredientService {

    fun streamIngredientClassificationForProduct(productId: UUID): Flux<String>

    /** Stream JSON response */
    fun streamIngredientClassificationForProductJson(productId: UUID): Flux<String>
}