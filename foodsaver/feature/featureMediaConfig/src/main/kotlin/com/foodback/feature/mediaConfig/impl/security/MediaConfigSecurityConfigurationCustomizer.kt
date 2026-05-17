package com.foodback.feature.mediaConfig.impl.security

import com.foodback.core.coreSecurity.api.service.SecurityConfigurationCustomizer
import org.springframework.context.annotation.Configuration

@Configuration
class MediaConfigSecurityConfigurationCustomizer: SecurityConfigurationCustomizer {

    override fun getPublicPaths(): List<String> {
        return listOf(
            "/media/**"
        )
    }
}