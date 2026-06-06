package com.foodback.feature.notification.impl.security

import com.foodback.core.coreSecurity.api.service.SecurityConfigurationCustomizer
import org.springframework.context.annotation.Configuration

@Configuration
internal class FirebaseSecurityConfigurationCustomizerImpl: SecurityConfigurationCustomizer {
    override fun getPublicPaths(): List<String> {
        return listOf(
            "/api/v1/firebase/token/**"
        )
    }
}