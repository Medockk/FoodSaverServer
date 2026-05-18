package com.foodback.feature.users.api.dto.proifle

import java.util.UUID

data class ProfileResponse(
    val id: UUID,
    val email: String,
    val fullName: String,
    val imageUri: String?,
    val restaurantId: UUID?,
    val authorities: List<String>,

    val phone: String?,
    val bio: String?,

    val addressIds: List<UUID>,
    val currentAddressId: UUID?,
    val currentPaymentMethodId: UUID?
)
