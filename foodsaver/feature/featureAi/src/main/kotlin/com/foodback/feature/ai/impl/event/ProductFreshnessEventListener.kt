package com.foodback.feature.ai.impl.event

import com.foodback.core.coreEvent.api.event.ai.ValidateProductFreshnessEvent
import com.foodback.feature.ai.api.service.AiImageAnalyzerService
import kotlinx.coroutines.runBlocking
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
internal class ProductFreshnessEventListener(
    private val aiImageAnalyzerService: AiImageAnalyzerService
) {

    @EventListener
    fun onValidateProductFreshness(event: ValidateProductFreshnessEvent) {
        runBlocking {
            try {
                val response = aiImageAnalyzerService.analyzeImage(
                    event.image,
                    event.imageName
                )

                event.result = ValidateProductFreshnessEvent.FreshnessResult(
                    isFresh = if (response.confidence < 85.0) false else response.isFresh,
                    label = response.label,
                    confidence = response.confidence,
                    action = response.action
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw e

            }
        }
    }
}