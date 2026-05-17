package com.foodback.feature.users.impl.service.auth

import com.foodback.core.coreSecurity.api.dto.UserRole
import com.foodback.feature.users.api.dto.auth.AuthResponse
import com.foodback.feature.users.api.dto.auth.LoginRequest
import com.foodback.feature.users.api.dto.auth.SignupRequest
import com.foodback.feature.users.api.service.auth.AuthService
import com.foodback.feature.users.impl.entity.UserEntity
import com.foodback.feature.users.impl.exception.auth.UserAlreadyRegisteredException
import com.foodback.feature.users.impl.exception.auth.UserNotFoundException
import com.foodback.feature.users.impl.repository.AuthRepository
import com.foodback.feature.users.impl.util.AuthResponseUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthServiceImpl(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authResponseUtil: AuthResponseUtil
) : AuthService {

    override fun login(request: LoginRequest): AuthResponse {
        validateCredentials(request.email, request.password)

        val user = authRepository.findByEmail(request.email)
            ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw UserNotFoundException()
        }

        return authResponseUtil.buildAuthResponse(user)
    }

    @Transactional
    override fun signup(request: SignupRequest): AuthResponse {
        validateCredentials(request.email, request.password)
        if (request.fullName.isBlank()) {
            throw IllegalArgumentException()
        }

        val isUserExist = authRepository.findByEmail(request.email) != null
        if (isUserExist) {
            throw UserAlreadyRegisteredException()
        }

        val passwordHash = passwordEncoder
            .encode(request.password)
            ?: throw IllegalArgumentException()

        val entityRequest = UserEntity(
            username = request.email,
            passwordHash = passwordHash,
            fullName = request.fullName,
            email = request.email,
            roles = mutableListOf(UserRole.USER)
        )

        val savedEntity = authRepository.save(entityRequest)
        return authResponseUtil.buildAuthResponse(savedEntity)
    }

    private fun validateCredentials(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            throw IllegalArgumentException("Credentials cannot be blank")
        }
    }
}