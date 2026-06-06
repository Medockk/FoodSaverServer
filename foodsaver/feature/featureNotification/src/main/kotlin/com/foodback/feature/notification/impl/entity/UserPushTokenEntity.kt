package com.foodback.feature.notification.impl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "user_push_tokens")
@EntityListeners(AuditingEntityListener::class)
internal class UserPushTokenEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val id: UUID? = null,

    @Column(nullable = false)
    var userId: UUID,
) {
    var firebaseToken: String? = null

    @UpdateTimestamp
    var updatedAt: Instant = Instant.now()
}