package com.foodback.feature.users.api.service

import com.foodback.feature.users.api.dto.AuthResponse
import com.foodback.feature.users.api.dto.LoginRequest
import com.foodback.feature.users.api.dto.SignupRequest

interface AuthService {

    fun login(request: LoginRequest): AuthResponse
    fun signup(request: SignupRequest): AuthResponse
}