package com.foodback.service.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service(value = "pushNotificationService")
class PushNotificationService: NotificationService {

    override fun sendNotification(recipient: String, message: String) {
        val notification = Notification.builder()
            .setTitle("FoodSaver")
            .setBody(message)
            .build()

        val firebaseMessage = Message.builder()
            .setNotification(notification)
            .setToken(recipient)
            .build()

        try {
            val response = FirebaseMessaging.getInstance().send(firebaseMessage)
            println("Response is $response")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}