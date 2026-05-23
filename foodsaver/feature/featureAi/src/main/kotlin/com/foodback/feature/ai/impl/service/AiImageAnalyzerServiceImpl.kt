package com.foodback.feature.ai.impl.service

import com.foodback.feature.ai.api.dto.ImageAnalyzeResponse
import com.foodback.feature.ai.api.service.AiImageAnalyzerService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
internal class AiImageAnalyzerServiceImpl(
    @Value($$"${ai.server.address}")
    private val aiServerAddress: String,
    @Value($$"${ai.server.port}")
    private val aiServerPort: String,
    @Value($$"${ai.server.protocol}")
    private val aiServerProtocol: String,
): AiImageAnalyzerService {

    private val webClient = WebClient
        .create()
    private val aiUri = "$aiServerProtocol://$aiServerAddress:$aiServerPort"

    override suspend fun analyzeImage(image: ByteArray, imageName: String): ImageAnalyzeResponse {
        val bodyBuilder = MultipartBodyBuilder()
        bodyBuilder.part("file", image)
            .filename(imageName)
            .contentType(MediaType.IMAGE_JPEG)

        return webClient.post()
            .uri("$aiUri/predict")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
            .retrieve()
            .awaitBody()
    }
}