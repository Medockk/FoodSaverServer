package com.foodback.feature.users.api.service.auth

import com.foodback.feature.users.api.dto.auth.AuthResponse
import com.foodback.feature.users.api.dto.auth.LoginRequest
import com.foodback.feature.users.api.dto.auth.SignupRequest

interface AuthService {

    fun login(request: LoginRequest): AuthResponse
    fun signup(request: SignupRequest): AuthResponse
}