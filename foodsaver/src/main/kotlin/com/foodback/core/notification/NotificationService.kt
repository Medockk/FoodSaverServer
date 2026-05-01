package com.foodback.core.notification

interface NotificationService {

    fun sendNotification(recipient: String, message: String, metadata: Map<String, String> = emptyMap())
}