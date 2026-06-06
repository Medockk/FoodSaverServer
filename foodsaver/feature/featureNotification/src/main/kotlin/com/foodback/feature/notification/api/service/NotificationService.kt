package com.foodback.feature.notification.api.service

interface NotificationService {

    fun send(recipient: String, message: String, metadata: Map<String, String>, sender: Any? = null)
}