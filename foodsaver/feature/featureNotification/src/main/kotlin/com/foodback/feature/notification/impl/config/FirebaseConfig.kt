package com.foodback.feature.notification.impl.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
internal class FirebaseConfig(
    @Value($$"${app.firebase.admin-sdk-path}")
    private val adminPath: String
) {

    @PostConstruct
    fun initializeFirebase() {
        val classPathInputStream = ClassPathResource(adminPath).inputStream
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(classPathInputStream))
            .build()

        FirebaseApp.initializeApp(options)
    }
}