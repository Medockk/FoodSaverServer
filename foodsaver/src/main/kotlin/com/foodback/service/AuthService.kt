package com.foodback.service

import com.foodback.dto.request.auth.NewPasswordRequest
import com.foodback.dto.request.auth.RefreshRequestModel
import com.foodback.dto.request.auth.SignInRequest
import com.foodback.dto.request.auth.SignUpRequest
import com.foodback.dto.response.auth.AuthResponse
import com.foodback.dto.response.auth.RefreshResponseModel
import com.foodback.entity.ResetPasswordEntity
import com.foodback.entity.User.Roles
import com.foodback.entity.User.UserEntity
import com.foodback.exception.auth.AuthenticationException
import com.foodback.exception.auth.UserException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.mappers.toAuthResponse
import com.foodback.repository.ResetPasswordRepository
import com.foodback.repository.UserRepository
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.security.jwt.JwtUtil
import com.foodback.utils.CookieUtil
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
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
 * @param userRepository Special object of [org.springframework.data.jpa.repository.JpaRepository] to get
 * access to table users in database
 * @param cookieUtil Utility to put jwt token to Cookie
 * @param authenticationManager special Spring manager to authenticate user
 * @param jwtUtil Special util to generate/verify access/refresh tokens
 * @param passwordEncoder Special encoder to generate encoded password and match raw password with encoded password
 * @param jwtExpirationMs expiration access token in milliseconds
 * @param resetPasswordRepository Special object of [org.springframework.data.jpa.repository.JpaRepository] to get
 *  * access to table reset_password_token in database
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

    /**
     * Method to authorize a new user
     * @param request Auth request
     * @param response Response to put access token to cookie
     */
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

    /**
     * Method to authenticate user
     * @param request Request with user data
     * @param response Response to put access token to cookie
     */
    fun signIn(
        request: SignInRequest,
        response: HttpServletResponse
    ): AuthResponse {
        val auth = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (_: org.springframework.security.core.AuthenticationException) {
            throw AuthenticationException(
                "Failed to authenticate user with username ${request.username}",
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

    /**
     * @param request Request with user data
     * @param response Response to put access token to cookie
     */
    fun refreshToken(
        request: RefreshRequestModel,
        response: HttpServletResponse
    ): RefreshResponseModel {

        if (request.accessToken != null && jwtUtil.validate(request.accessToken)) {
            throw AuthenticationException("Jwt token is not expires", RequestError.Authentication.JWT_TOKEN_NOT_EXPIRED)
        }

        if (!jwtUtil.validate(request.refreshToken)) {
            throw AuthenticationException("Refresh token expires", RequestError.Authentication.REFRESH_TOKEN_EXPIRED)
        }

        val username = jwtUtil.getUsername(request.refreshToken)
        val accessToken = jwtUtil.generateAccessToken(username)
        response.addJwtCookie(accessToken)

        return RefreshResponseModel(
            jwtToken = accessToken,
            expiresIn = jwtExpirationMs
        )
    }

    /**
     * Method to reset password with email. This method check, does user with [email] exist,
     * and send to this [email] link to reset password
     * @param email user email
     */
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

    /**
     * Method to reset password with special [token].
     * @param token RESET-TOKEN
     */
    @Transactional
    fun resetPassword(
        token: UUID,
        request: NewPasswordRequest
    ) {
        val entity: ResetPasswordEntity = resetPasswordRepository.findByResetToken(token).orElseThrow {
            UserException("Token expires or wrong", HttpStatus.BAD_REQUEST, RequestError.Authentication.RESET_TOKEN_NOT_FOUND)
        }
        entity.isUsed = true

        val user = entity.uid ?: throw UserException("Reset token linked to a null user", RequestError.Authentication.RESET_TOKEN_LINKED_TO_NULL)
        val passwordHash = passwordEncoder.encode(/* rawPassword = */ request.password)
        user.passwordHash = passwordHash
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