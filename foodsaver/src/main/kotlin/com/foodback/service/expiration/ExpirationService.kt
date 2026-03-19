package com.foodback.service.expiration

import com.foodback.app.cart.repository.CartItemRepository
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
    private val userRepository: UserRepository,
    @Qualifier("pushNotificationService")
    private val notificationService: NotificationService
) {

    @Transactional
    @Scheduled(fixedRate = 60_000)
    fun processExpirationProducts() {
        val now = Instant.now()
        val threshold = now.plus(Duration.ofHours(2L))

        val itemsToNotify = cartItemRepository
            .findAllByProductExpiresAtBefore(threshold)
            .getOrElse { emptyList() }

        //notify users
        itemsToNotify.forEach {
            val uid = it.cart.uid
            val user = userRepository.findUserById(uid)

            user.fcmTokensEntity.forEach { tokenEntity ->
                notificationService.sendNotification(
                    recipient = tokenEntity.token,
                    message = "Product ${it.product.title} expires very soon!"
                )
            }
        }

        productRepository.deleteProductAfterExpiresAt(now)
    }
}