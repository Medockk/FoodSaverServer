package com.foodback.app.notificationLog.repository

import com.foodback.app.notificationLog.entity.NotificationLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NotificationLogRepository: JpaRepository<NotificationLogEntity, UUID> {
}