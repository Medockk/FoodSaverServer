package com.foodback.app.firebase.entity

import com.foodback.app.user.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
@Table(name = "fcm_tokens")
class FCMTokensEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var id: UUID? = null,

    @Column(nullable = false)
    var token: String = "",

    @ManyToOne
    @JoinColumn(name = "user_uid", nullable = false)
    var user: UserEntity? = null
)