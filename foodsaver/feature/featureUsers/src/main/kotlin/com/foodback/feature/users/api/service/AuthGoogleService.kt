package com.foodback.feature.users.api.service

import com.foodback.feature.users.api.dto.AuthResponse
import com.foodback.feature.users.api.dto.GoogleRequest

interface AuthGoogleService {

    fun authorizeWithGoogle(request: GoogleRequest): AuthResponse
}