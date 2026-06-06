package com.foodback.feature.notification.impl.service

import com.foodback.feature.notification.api.service.NotificationService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service("push")
internal class PushNotificationServiceImpl: NotificationService {

    override fun send(
        recipient: String,
        message: String,
        metadata: Map<String, String>,
        sender: Any?
    ) {
        val notification = Notification.builder()
            .setTitle("FoodSaver")
            .setBody(message)
            .build()

        val firebaseMessage = Message.builder()
            .putAllData(metadata)
            .setNotification(notification)
            .setToken(recipient)

        try {
            val message = firebaseMessage.build()
            val response = FirebaseMessaging.getInstance().send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}