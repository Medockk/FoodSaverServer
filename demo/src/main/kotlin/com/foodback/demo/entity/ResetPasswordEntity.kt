package com.foodback.demo.entity

import com.foodback.demo.entity.User.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

/**
 * Special database entity
 * @param id unique identifier in format [UUID]
 * @param resetToken unique RESET-TOKEN to reset password
 * @param uid user identifier, which own [resetToken]
 * @param expiresAt Date, when this [resetToken] ceases to operate
 * @param isUsed Flag, if this [resetToken] already used
 */
@Entity
@Table(name = "reset_password_token")
data class ResetPasswordEntity(

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(nullable = false, unique = true)
    var resetToken: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    var uid: UserEntity? = null,

    @Column(nullable = false)
    var expiresAt: Instant = Instant.now(),

    @Column(nullable = false)
    var isUsed: Boolean = false
)
