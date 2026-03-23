package com.foodback.service.expiration

import com.foodback.app.cart.repository.CartItemRepository
import com.foodback.app.notificationLog.entity.NotificationLogEntity
import com.foodback.app.notificationLog.repository.NotificationLogRepository
import com.foodback.app.product.repository.ProductRepository
import com.foodback.app.user.repository.UserRepository
import com.foodback.service.notification.NotificationService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import kotlin.jvm.optionals.getOrElse

@Component
class ExpirationService(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository,
    private val notificationLogRepository: NotificationLogRepository,
    @Qualifier("pushNotificationService")
    private val notificationService: NotificationService
) {

    @Transactional
    @Scheduled(fixedRate = 60_000)
    fun processExpirationProducts() {
        val now = Instant.now()
        val threshold = now.plus(Duration.ofHours(2L))

        val itemsToNotify = cartItemRepository
            .findAllExpiredToNotify(threshold)

        //notify users
        itemsToNotify.forEach { item ->
            val user = item.cart.user
            val product = item.product

            user?.fcmTokensEntity?.forEach { tokenEntity ->
                try {
                    val metadata = mapOf(
                        "product_id" to product.id.toString()
                    )

                    notificationService.sendNotification(
                        recipient = tokenEntity.token,
                        message = "Product ${product.title} expires very soon!",
                        metadata = metadata
                    )

                    notificationLogRepository.save(NotificationLogEntity(
                        uid = user.uid!!,
                        product = product,
                        notificationType = NotificationLogEntity.NotificationType.EXPIRATION_WARNING.name
                    ))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        productRepository.deleteProductAfterExpiresAt(now)
    }
}