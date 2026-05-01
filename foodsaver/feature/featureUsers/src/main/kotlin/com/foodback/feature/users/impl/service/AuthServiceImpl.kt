package com.foodback.feature.users.impl.service

import com.foodback.core.coreSecurity.api.dto.UserRole
import com.foodback.feature.users.api.dto.AuthResponse
import com.foodback.feature.users.api.dto.LoginRequest
import com.foodback.feature.users.api.dto.SignupRequest
import com.foodback.feature.users.api.service.AuthService
import com.foodback.feature.users.impl.entity.UserEntity
import com.foodback.feature.users.impl.exception.UserAlreadyRegisteredException
import com.foodback.feature.users.impl.exception.UserNotFoundException
import com.foodback.feature.users.impl.repository.UserRepository
import com.foodback.feature.users.impl.util.AuthResponseUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authResponseUtil: AuthResponseUtil
) : AuthService {

    override fun login(request: LoginRequest): AuthResponse {
        validateCredentials(request.email, request.password)

        val user = userRepository.findByEmail(request.email)
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

        val isUserExist = userRepository.findByEmail(request.email) != null
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

        val savedEntity = userRepository.save(entityRequest)
        return authResponseUtil.buildAuthResponse(savedEntity)
    }

    private fun validateCredentials(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            throw IllegalArgumentException("Credentials cannot be blank")
        }
    }
}