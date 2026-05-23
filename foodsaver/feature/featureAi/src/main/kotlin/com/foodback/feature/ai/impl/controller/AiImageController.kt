package com.foodback.feature.ai.impl.controller

import com.foodback.feature.ai.api.dto.ImageAnalyzeResponse
import com.foodback.feature.ai.api.service.AiImageAnalyzerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/ai/image")
internal class AiImageController(
    private val aiImageAnalyzerService: AiImageAnalyzerService
) {

    @PostMapping("/analyze")
    suspend fun analyzeFoodImageForFreshState(
        @RequestPart file: MultipartFile
    ): ResponseEntity<ImageAnalyzeResponse> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().build()
        }

        val response = aiImageAnalyzerService.analyzeImage(
            image = file.bytes,
            imageName = file.originalFilename ?: "food_image.png"
        )

        return ResponseEntity.ok(response)
    }
}