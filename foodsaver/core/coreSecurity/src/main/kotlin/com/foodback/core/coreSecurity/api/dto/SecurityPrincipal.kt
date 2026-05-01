package com.foodback.core.coreSecurity.api.dto

import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface SecurityPrincipal: UserDetails {

    val uid: UUID
    val restaurantId: UUID?
}