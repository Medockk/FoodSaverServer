package com.foodback.config

import com.foodback.exception.handler.GlobalExceptionHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Class-configuration to CORS Policy
 * This configuration is applied at the Spring MVC level
 * and is guaranteed to be executed before Spring Security starts processing requests.
 */
@Configuration
class WebConfig(
    private val handler: GlobalExceptionHandler,
    @Value($$"${app.media.root}")
    private val resourceLocation: String
) : WebMvcConfigurer {

    @Bean
    fun getAsyncThreadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        executor.maxPoolSize = 15
        executor.setThreadNamePrefix("ai-executor-")
        executor.initialize()
        return executor
    }

    override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
        configurer.setTaskExecutor(DelegatingSecurityContextAsyncTaskExecutor(
            getAsyncThreadPoolTaskExecutor())
        )
        configurer.setDefaultTimeout(90_000)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8087", "http://mylocalhost.com:8087", "http://10.198.60.227:8087")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600L)
    }

    override fun extendHandlerExceptionResolvers(resolvers: MutableList<HandlerExceptionResolver>) {
        resolvers.add(0, handler)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("media/**")
            .addResourceLocations("file:///$resourceLocation/")
    }
}