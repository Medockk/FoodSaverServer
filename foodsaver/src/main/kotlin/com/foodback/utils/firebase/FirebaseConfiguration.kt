package com.foodback.utils.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirebaseConfiguration(
    @Value($$"${app.firebase.admin-sdk-path}")
    private val firebaseAdminSdkPath: String
) {

    @PostConstruct
    fun initialize() {
        if (FirebaseApp.getApps().isEmpty()) {
            val classPathResource = ClassPathResource(firebaseAdminSdkPath)
            val firebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(classPathResource.inputStream))
                .build()

            FirebaseApp.initializeApp(firebaseOptions)
        }

    }
}