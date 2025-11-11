package com.foodback.demo.service

import com.foodback.demo.dto.request.auth.RefreshRequestModel
import com.foodback.demo.dto.request.auth.SignInRequest
import com.foodback.demo.dto.request.auth.SignUpRequest
import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.dto.response.auth.RefreshResponseModel
import com.foodback.demo.entity.ResetPasswordEntity
import com.foodback.demo.entity.User.Roles
import com.foodback.demo.entity.User.UserEntity
import com.foodback.demo.exception.auth.AuthenticationException
import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.exception.general.ErrorCode.RequestError
import com.foodback.demo.mappers.toAuthResponse
import com.foodback.demo.repository.ResetPasswordRepository
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
import java.time.Instant
import java.util.*

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
    private val jwtExpirationMs: Long,

    private val resetPasswordRepository: ResetPasswordRepository,
) {

    @Transactional
    fun signUp(
        request: SignUpRequest,
        response: HttpServletResponse
    ): AuthResponse {
        if (userRepository.findByUsername(request.username) != null) {
            throw AuthenticationException("User already registered", RequestError.Authentication.USERNAME_OCCUPIED)
        }

        val entity = UserEntity(
            username = request.username,
            name = request.displayName,
            passwordHash = passwordEncoder.encode(request.password),
            roles = mutableListOf(Roles.USER.name)
        )

        val user = userRepository.save(entity)
        val accessToken = jwtUtil.generateAccessToken(user.username)
        val refreshToken = jwtUtil.generateRefreshToken(user.username)
        response.addJwtCookie(accessToken)

        if (user.uid != null) {
            return user.toAuthResponse(user.uid!!, accessToken, refreshToken, jwtExpirationMs)
        } else {
            throw AuthenticationException(RequestError.Authentication.FAILED_REGISTER_USER)
        }
    }

    fun signIn(
        request: SignInRequest,
        response: HttpServletResponse
    ): AuthResponse {
        val auth = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (e: org.springframework.security.core.AuthenticationException) {
            throw AuthenticationException(
                "Failed to authenticate user",
                RequestError.Authentication.FAILED_AUTHORIZE_USER
            )
        }
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
            throw AuthenticationException("Jwt token is not expires", RequestError.Authentication.JWT_TOKEN_NOT_EXPIRED)
        }

        if (!jwtUtil.validate(request.refreshToken)) {
            throw AuthenticationException("Refresh token expires", RequestError.Authentication.REFRESH_TOKEN_EXPIRED)
        }

        val username = jwtUtil.getUsername(request.accessToken)
        val accessToken = jwtUtil.generateAccessToken(username)
        response.addJwtCookie(accessToken)

        return RefreshResponseModel(
            jwtToken = accessToken,
            expiresIn = jwtExpirationMs
        )
    }

    @Transactional
    fun resetPassword(
        email: String
    ): UUID {
        val user = userRepository.findByEmail(email).orElseThrow {
            UserException("User with email $email not found!", RequestError.UserRequest.EMAIL_NOT_FOUND)
        }

        val expiresAt = Instant.ofEpochMilli(System.currentTimeMillis() + 1_800_000)
        println(expiresAt)
        val resetPasswordEntity = ResetPasswordEntity(
            resetToken = UUID.randomUUID(),
            uid = user,
            expiresAt = expiresAt,
            isUsed = false
        )
        return resetPasswordRepository.save(resetPasswordEntity).resetToken
    }

    @Transactional
    fun resetPassword(
        token: UUID
    ) {
        val entity = resetPasswordRepository.findByResetToken(token).orElseThrow {
            UserException("Token expires or wrong")
        }
        entity.isUsed = true
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