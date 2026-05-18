package com.foodback.feature.users.impl.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID

@Entity
@Table(name = "profiles")
@EntityListeners(AuditingEntityListener::class)
internal class ProfileEntity(
    @Id
    val userId: UUID,

    @Column(nullable = false)
    var fullName: String = "",

    var phone: String? = null,
    var bio: String? = null,
    var imageUri: String? = null,

    @CollectionTable(
        name = "profile_addresses",
        joinColumns = [JoinColumn(name = "profile_id")]
    )
    @ElementCollection(fetch = FetchType.LAZY)
    var addressIds: MutableList<UUID> = mutableListOf(),
    var currentAddressId: UUID? = null,
    var currentPaymentMethodId: UUID? = null
)