package com.foodback.config

import com.foodback.exception.handler.GlobalExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Class-configuration to CORS Policy
 * This configuration is applied at the Spring MVC level
 * and is guaranteed to be executed before Spring Security starts processing requests.
 */
@Configuration
class WebConfig(
    private val handler: GlobalExceptionHandler
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8087", "http://mylocalhost.com:8087", "http://10.152.104.227:8087")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600L)
    }

    override fun extendHandlerExceptionResolvers(resolvers: MutableList<HandlerExceptionResolver>) {
        resolvers.add(0, handler)
    }
}