package com.foodback.feature.users.api.service.auth

import com.foodback.feature.users.api.dto.auth.AuthResponse
import com.foodback.feature.users.api.dto.auth.GoogleRequest

interface AuthGoogleService {

    fun authorizeWithGoogle(request: GoogleRequest): AuthResponse
}