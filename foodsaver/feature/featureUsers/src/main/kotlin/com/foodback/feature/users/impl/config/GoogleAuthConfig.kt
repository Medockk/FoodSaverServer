package com.foodback.feature.users.impl.config

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class GoogleAuthConfig(
    @Value($$"${spring.security.oauth2.client.registration.google.client-id.android}")
    private val androidGoogleId: String,
    @Value($$"${spring.security.oauth2.client.registration.google.client-id.desktop}")
    private val desktopGoogleId: String,
    @Value($$"${spring.security.oauth2.client.registration.google.client-id.web}")
    private val webGoogleId: String,
) {

    @Bean
    fun googleIdTokenVerifier(): GoogleIdTokenVerifier {
        val transport = NetHttpTransport()
        val jsonFactory = GsonFactory()

        return GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(listOf(androidGoogleId, desktopGoogleId, webGoogleId))
            .build()
    }
}