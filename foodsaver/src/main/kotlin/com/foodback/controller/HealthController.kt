package com.foodback.controller

import com.foodback.core.coreCommon.api.error.GlobalErrorResponse
import com.foodback.core.coreSecurity.api.service.SecurityConfigurationCustomizer
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
internal class HealthController {

    @Operation(
        summary = "Get simple server health"
    )
    @ApiResponse(
        content = [
            Content(
                schema = Schema(implementation = GlobalErrorResponse::class)
            )
        ]
    )
    @GetMapping("/health")
    fun getHealth(): ResponseEntity<String> {
        return ResponseEntity
            .ok("I'm alive")
    }
}

@Configuration
internal class HealthSecurityCustomizerConfiguration: SecurityConfigurationCustomizer {

    override fun getPublicPaths(): List<String> {
        return listOf(
            "/health"
        )
    }
}