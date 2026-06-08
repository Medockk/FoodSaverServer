package com.foodback.feature.expiration.impl.service

import com.foodback.feature.expiration.api.dto.ExpirationAlert
import com.foodback.feature.notification.api.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class ExpirationService(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    @Qualifier("push")
    private val notificationService: NotificationService
) {
    private val log = LoggerFactory.getLogger(ExpirationService::class.java)
    private val sentNotificationsCache = ConcurrentHashMap.newKeySet<String>()

    @Scheduled(fixedRate = 60_000) // Раз в минуту
    fun processExpirationProducts() {
        val now = Instant.now()
        val threshold = now.plus(Duration.ofHours(2L))

        log.info("Сканирование истекающих продуктов через Native SQL...")

        val sql = """
            SELECT t.firebase_token AS token, p.name AS product_name, p.id AS product_id
            FROM public.cart_items ci
            JOIN public.carts c ON ci.cart_id = c.id
            JOIN public.products p ON ci.product_id = p.id
            JOIN public.user_push_tokens t ON c.user_id = t.user_id
            WHERE p.expires_at BETWEEN :now AND :threshold
              AND p.is_deleted = false
              AND p.is_available = true
        """.trimIndent()

        // Передаем параметры в запрос
        val params = mapOf(
            "now" to Timestamp.from(now),
            "threshold" to Timestamp.from(threshold)
        )

        // Выполняем запрос и маппим результат в DTO
        val alerts = jdbcTemplate.query(sql, params) { rs, _ ->
            ExpirationAlert(
                token = rs.getString("token"),
                productName = rs.getString("product_name"),
                productId = UUID.fromString(rs.getString("product_id"))
            )
        }

        if (alerts.isEmpty()) {
            log.info("Nothing not found in cart.")
            if (sentNotificationsCache.isNotEmpty()) {
                sentNotificationsCache.clear()
            }
            return
        }

        // Отправляем пуши
        alerts.forEach { alert ->
            val cacheKey = "${alert.token}_${alert.productId}"

            if (!sentNotificationsCache.contains(cacheKey)) {
                log.info("Отправка push-уведомления для токена ...${alert.token.takeLast(8)}")

                notificationService.send(
                    recipient = alert.token,
                    message = "Продукт '${alert.productName}' в вашей корзине скоро спишется по сроку годности! Успейте заказать ⏳",
                    metadata = mapOf(
                        "product_id" to alert.productId.toString(),
                        "product_name" to alert.productName
                    )
                )

                sentNotificationsCache.add(cacheKey)
            }
        }
    }
}