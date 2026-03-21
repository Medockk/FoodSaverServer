package com.foodback.entity.User

import com.foodback.entity.AddressEntity
import com.foodback.entity.OrganizationEntity
import com.foodback.entity.PaymentMethodEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.util.*

/**
 * Special database entity
 * @param uid Unique user identifier
 * @param username Username of current user
 * @param passwordHash Hash of User password
 * @param name Name of current user. Can be null
 * @param email Email address of current user. Can be null
 * @param photoUrl Url to user avatar
 * @param createdAt Date of creating new user
 * @param updatedAt Date of last update user data
 * @param roles Special roles of current user. This param is linked to other table in database
 * @param organization if not null, then the organization to which the user belongs
 * @param googleId Identifier of Google account.
 */
@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    var uid: UUID? = null,

    @Column(nullable = false)
    var username: String = "",
    @Column(nullable = true)
    var passwordHash: String? = "",

    @Column(unique = true, nullable = true)
    @field:Email
    var email: String? = null,

    @Column(nullable = true)
    var name: String? = "",
    @Column(nullable = true)
    var photoUrl: String? = null,

    @CreatedDate
    var createdAt: Instant = Instant.now(),
    @LastModifiedDate
    var updatedAt: Instant = Instant.now(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_uid")],
        foreignKey = ForeignKey(ConstraintMode.CONSTRAINT)
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "role")
    var roles: MutableList<String> = mutableListOf(Roles.USER.name),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    var organization: OrganizationEntity? = null,

    @Column(nullable = true)
    var googleId: String? = null,

    @Column(nullable = true)
    var phone: String? = null,

    @Column(nullable = true)
    var bio: String? = null,

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        mappedBy = "user"
    )
    var addresses: MutableList<AddressEntity> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "user")
    var paymentMethods: MutableList<PaymentMethodEntity> = mutableListOf()
)
