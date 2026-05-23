package com.foodback.feature.ai.impl.service

import com.foodback.feature.ai.api.service.AiIngredientService
import com.foodback.feature.featureIngredients.api.service.ReadIngredientsService
import com.foodback.feature.featureProduct.api.service.ReadProductService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.UUID

@Service
internal class AiIngredientServiceImpl(
    private val readProductService: ReadProductService,
    private val readIngredientsService: ReadIngredientsService,

    private val aiIngredientsServiceAnalyst: AiIngredientsServiceAnalyst
): AiIngredientService {

    override fun streamIngredientClassificationForProduct(productId: UUID): Flux<String> {
        val product = readProductService.getProductById(productId)
        if (product.ingredientIds.isEmpty()) return Flux.just("У этого продукта не указан состав")

        val ingredients = readIngredientsService.getIngredientsByIds(product.ingredientIds)
        val ingredientsText = ingredients.joinToString { it.name }

        val sinks = Sinks.many().unicast().onBackpressureBuffer<String>()
        val tokenStream = aiIngredientsServiceAnalyst
            .analyzeIngredients(ingredientsText)

        tokenStream
            .onNext { sinks.tryEmitNext(it) }
            .onComplete { sinks.tryEmitComplete() }
            .onError { sinks.tryEmitError(it) }
            .start()

        return sinks.asFlux()
    }

    override fun streamIngredientClassificationForProductJson(productId: UUID): Flux<String> {
        val product = readProductService.getProductById(productId)
        if (product.ingredientIds.isEmpty()) return Flux.just("У этого продукта не указан состав")

        val ingredients = readIngredientsService.getIngredientsByIds(product.ingredientIds)
        val ingredientsText = ingredients.joinToString { it.name }

        val sinks = Sinks.many().unicast().onBackpressureBuffer<String>()
        val tokenStream = aiIngredientsServiceAnalyst.analyzeIngredientsJson(ingredientsText)
        println("Генерация ИИ описания состава продукта")

        tokenStream
            .onNext {
                print(it)
                sinks.tryEmitNext(it)
            }
            .onComplete {
                println("ИИ генерация состава завершена $it")
                sinks.tryEmitComplete()
            }
            .onError {
                println("ИИ генерация состава завершена с ошибкой $it")
                sinks.tryEmitError(it)
            }
            .start()

        return sinks.asFlux()
    }
}