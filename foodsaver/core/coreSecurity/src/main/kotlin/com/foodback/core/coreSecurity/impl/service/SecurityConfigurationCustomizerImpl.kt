package com.foodback.core.coreSecurity.impl.service

import com.foodback.core.coreSecurity.api.service.SecurityConfigurationCustomizer
import org.springframework.context.annotation.Configuration

@Configuration
internal class SecurityConfigurationCustomizerImpl : SecurityConfigurationCustomizer {

    override fun getPublicPaths(): List<String> {
        return listOf(
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/actuator/**"
        )
    }
}