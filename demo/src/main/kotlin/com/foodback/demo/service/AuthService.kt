package com.foodback.demo.service

import com.foodback.demo.dto.request.auth.RefreshRequestModel
import com.foodback.demo.dto.request.auth.SignInRequest
import com.foodback.demo.dto.request.auth.SignUpRequest
import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.dto.response.auth.RefreshResponseModel
import com.foodback.demo.entity.User.Roles
import com.foodback.demo.entity.User.UserEntity
import com.foodback.demo.mappers.toResponse
import com.foodback.demo.repository.UserRepository
import com.foodback.demo.security.JwtUtil
import com.foodback.demo.security.UserDetailsImpl
import com.foodback.demo.utils.CookieUtil
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service to Authenticate user and update JWT token
 * @param cookieUtil Utility to put jwt token to Cookie
 */
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val cookieUtil: CookieUtil,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: PasswordEncoder,
    @Value($$"${jwt.expiration.ms}")
    private val jwtExpirationMs: Long
) {

    @Transactional
    fun signUp(
        request: SignUpRequest,
        response: HttpServletResponse
    ): AuthResponse {
        if (userRepository.findByUsername(request.email) != null) {
            throw IllegalArgumentException("User already registered")
        }

        val entity = UserEntity(
            username = request.email,
            name = request.displayName,
            passwordHash = passwordEncoder.encode(request.password),
            roles = mutableListOf(Roles.USER.name)
        )

        val user = userRepository.save(entity)
        val accessToken = jwtUtil.generateAccessToken(user.username)
        val refreshToken = jwtUtil.generateRefreshToken(user.username)
        response.addJwtCookie(accessToken)

        if (user.uid != null) {
            return user.toResponse(user.uid!!,accessToken, refreshToken, jwtExpirationMs)
        } else {
            throw IllegalArgumentException("Failed to save user")
        }
    }

    fun signIn(
        request: SignInRequest,
        response: HttpServletResponse
    ): AuthResponse {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        SecurityContextHolder.getContext().authentication = auth

        val principal = auth.principal as UserDetailsImpl
        val username = principal.username

        val accessToken = jwtUtil.generateAccessToken(username)
        val refreshToken = jwtUtil.generateRefreshToken(username)
        response.addJwtCookie(accessToken)

        return AuthResponse(
            uid = principal.uid,
            username = username,
            roles = principal.authorities.mapNotNull { it.authority },
            jwtToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpirationMs
        )
    }

    fun refreshToken(
        request: RefreshRequestModel,
        response: HttpServletResponse
    ): RefreshResponseModel {

        if (jwtUtil.validate(request.accessToken)) {
            throw IllegalArgumentException("Jwt token is not expires")
        }

        if (!jwtUtil.validate(request.refreshToken)) {
            throw IllegalArgumentException("Refresh token expires")
        }

        val username = jwtUtil.getUsername(request.accessToken)
        val accessToken = jwtUtil.generateAccessToken(username)
        response.addJwtCookie(accessToken)

        return RefreshResponseModel(
            jwtToken = accessToken,
            expiresIn = jwtExpirationMs
        )
    }

    /**
     * Method to create and add JWT token to cookie
     * @param jwt JWT Token
     */
    private fun HttpServletResponse.addJwtCookie(jwt: String) {
        val cookie = cookieUtil.createJwtCookie(jwt)
        addCookie(cookie)
    }
}