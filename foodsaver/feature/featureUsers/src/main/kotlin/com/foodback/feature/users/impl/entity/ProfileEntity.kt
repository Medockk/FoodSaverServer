package com.foodback.feature.users.impl.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@Entity
@Table(name = "profiles")
@EntityListeners(AuditingEntityListener::class)
internal class ProfileEntity(
    @Id
    val userId: UUID,

    @Column(nullable = false)
    var fullName: String = "",
) {
    var phone: String? = null
    var bio: String? = null
    var imageUri: String? = null

    @CollectionTable(
        name = "profile_addresses",
        joinColumns = [JoinColumn(name = "profile_id")]
    )
    @ElementCollection(fetch = FetchType.LAZY)
    var addressIds: MutableList<UUID> = mutableListOf()
    var currentAddressId: UUID? = null
    var currentPaymentMethodId: UUID? = null
}