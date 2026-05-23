package com.foodback.feature.ai.api.service

import com.foodback.feature.ai.api.dto.ImageAnalyzeResponse

interface AiImageAnalyzerService {

    suspend fun analyzeImage(image: ByteArray, imageName: String): ImageAnalyzeResponse
}