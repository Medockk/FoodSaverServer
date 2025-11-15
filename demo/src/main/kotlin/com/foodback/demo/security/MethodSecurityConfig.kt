package com.foodback.demo.security

import com.foodback.demo.utils.OrganizationPermissionEvaluator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

/**
 * Configuration to enable roles and permissions to this roles
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig(
    private val organizationPermissionEvaluator: OrganizationPermissionEvaluator
) {

    @Bean
    fun methodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        expressionHandler.setPermissionEvaluator(organizationPermissionEvaluator)

        return expressionHandler
    }
}