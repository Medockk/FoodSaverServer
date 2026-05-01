package com.foodback.feature.users.impl.security

import com.foodback.core.coreSecurity.api.service.SecurityConfigurationCustomizer
import org.springframework.context.annotation.Configuration

@Configuration
internal class AuthSecurityConfigurationCustomizer: SecurityConfigurationCustomizer {

    override fun getPublicPaths(): List<String> {
        return listOf(
            "/api/v1/auth/**",
            "/api/v1/refreshToken"
        )
    }
}