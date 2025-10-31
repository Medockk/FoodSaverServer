package com.foodback.demo.config

import com.foodback.demo.controller.ChatWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.*

/**
 * Web Socket configuration
 */
@Configuration
@EnableWebSocketMessageBroker
class WebSocketMessageBrokerConfig: WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
        registry.setUserDestinationPrefix("/user")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/wsmb")
            .setAllowedOriginPatterns("*")
    }
}

@Configuration
@EnableWebSocket
class WebSocketConfig: WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(ChatWebSocketHandler(), "/ws")
            .setAllowedOrigins("*")
    }
}