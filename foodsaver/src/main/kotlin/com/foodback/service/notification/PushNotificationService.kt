package com.foodback.service.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service(value = "pushNotificationService")
class PushNotificationService: NotificationService {

    override fun sendNotification(recipient: String, message: String, metadata: Map<String, String>) {
        val notification = Notification.builder()
            .setTitle("FoodSaver")
            .setBody(message)
            .build()

        val firebaseMessageBuilder = Message.builder()
            .setNotification(notification)
            .setToken(recipient)

        // add all metadata to request
        firebaseMessageBuilder.putAllData(metadata)

        try {
            val response = FirebaseMessaging.getInstance().send(firebaseMessageBuilder.build())
            println("Response is $response")
            println("Response to recipient $recipient")
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}