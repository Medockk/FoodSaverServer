package com.foodback.app.notificationLog.entity

import com.foodback.app.product.entity.ProductEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import java.util.*

@Entity
@Table(name = "notification_logs")
class NotificationLogEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(name = "user_uid")
    var uid: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OnDelete(OnDeleteAction.CASCADE)
    var product: ProductEntity,

    var notificationType: String = NotificationType.EXPIRATION_WARNING.name,

    @CreatedDate
    var sentAt: Instant = Instant.now()
) {
    enum class NotificationType {
        EXPIRATION_WARNING
    }
}