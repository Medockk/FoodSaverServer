package com.foodback.demo.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

/**
 * Configuration to enable roles and permissions to this roles
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig