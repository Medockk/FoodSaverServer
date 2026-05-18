package com.foodback.feature.users.impl.entity

import com.foodback.core.coreSecurity.api.dto.UserRole
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "auth")
internal class AuthEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    val uid: UUID? = null,
    
    val username: String = "",
    var email: String = "",

    var passwordHash: String? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn("user_id")])
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    var roles: MutableList<UserRole> = mutableListOf(UserRole.USER),

    var restaurantId: UUID? = null,
    var googleId: String? = null
)