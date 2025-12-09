package com.foodback.dto.response.user

import com.foodback.dto.response.address.AddressResponseModel
import java.time.Instant
import java.util.*

/**
 * Response model of user data
 * @param uid unique user identifier
 * @param username unique username
 * @param email unique user email address
 * @param name user name
 * @param photoUrl URL to user avatar
 * @param createdAt Date when current user created
 * @param roles Authority and roles of current user
 */
data class UserResponseModel(
    val uid: UUID,
    val username: String,
    val email: String?,
    val name: String?,
    val photoUrl: String?,
    val createdAt: Instant,
    val roles: List<String>,

    val phone: String?,
    val bio: String?,
    val addresses: List<AddressResponseModel> = emptyList(),
    val paymentCartNumbers: List<PaymentCardResponse> = emptyList()
)
