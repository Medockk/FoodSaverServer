package com.foodback.core.coreEvent.api.event.ai

class ValidateProductFreshnessEvent(
    val image: ByteArray,
    val imageName: String,

    var result: FreshnessResult? = null
) {

    data class FreshnessResult(
        val isFresh: Boolean,
        val label: String,
        val confidence: Double,
        val action: String
    )
}