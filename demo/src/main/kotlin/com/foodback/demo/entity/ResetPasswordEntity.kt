package com.foodback.demo.entity

import com.foodback.demo.entity.User.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

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
