package com.foodback.app.ingredients.controller

import com.foodback.app.ai.service.AiServiceAnalyst
import com.foodback.app.ingredients.service.IngredientsService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@RestController
@RequestMapping("/api/v1/ingredients/ai")
class AiIngredientsController(
    private val aiServiceAnalyst: AiServiceAnalyst,
    private val ingredientsService: IngredientsService,
    @Qualifier("aiTaskExecutor")
    private val executor: Executor
) {

    @GetMapping("/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun analyzeIngredients(@RequestParam productId: UUID): SseEmitter? {
        val ingredients = (ingredientsService.getIngredientsByProductId(productId)
            ?: return null)
            .ingredients.ifEmpty { return null }

        val emitter = SseEmitter(60_000)
        CompletableFuture.runAsync( {
            try {
                aiServiceAnalyst.analyzeIngredients(ingredients.joinToString())
                    .onNext { chunk ->
                        emitter.send(SseEmitter.event().data(chunk))
                    }.onComplete {
                        emitter.send(SseEmitter.event().name("complete").data("Done"))
                        emitter.complete()
                    }.onError {
                        it.printStackTrace()
                    }
                    .start()
            } catch (e: Exception) {
                emitter.completeWithError(e)
                e.printStackTrace()
            }
        }, executor)
        return emitter
    }
}