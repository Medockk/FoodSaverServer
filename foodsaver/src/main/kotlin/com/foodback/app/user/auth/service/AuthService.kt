package com.foodback.app.user.auth.service

import com.foodback.app.user.auth.dto.request.NewPasswordRequestV1
import com.foodback.app.user.auth.dto.request.RefreshRequestModelV1
import com.foodback.app.user.auth.dto.request.SignInRequestV1
import com.foodback.app.user.auth.dto.request.SignUpRequestV1
import com.foodback.app.user.auth.dto.response.AuthResponseV1
import com.foodback.app.user.auth.dto.response.GoogleAuthResponseV1
import com.foodback.app.user.auth.dto.response.RefreshResponseModelV1
import com.foodback.app.user.entity.Roles
import com.foodback.app.user.entity.UserEntity
import com.foodback.app.user.mapper.UserMapperV1
import com.foodback.app.user.auth.entity.ResetPasswordEntity
import com.foodback.exception.auth.AuthenticationException
import com.foodback.exception.auth.UserException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.app.user.auth.repository.ResetPasswordRepository
import com.foodback.app.user.repository.UserRepository
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.security.jwt.JwtUtil
import com.foodback.utils.CookieUtil
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
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
    @Value($$"${spring.security.oauth2.client.registration.google.client-id.web}")
    private val webClientId: String,
    @Value($$"${spring.security.oauth2.client.registration.google.client-id.android}")
    private val androidClientId: String,
    @Value($$"${spring.security.oauth2.client.registration.google.client-id.desktop}")
    private val desktopClientId: String,

    private val userMapperV1: UserMapperV1
) {

    /**
     * Method to authorize a new user
     * @param request Auth request
     * @param response Response to put access token to cookie
     */
    @Transactional
    fun signUp(
        request: SignUpRequestV1,
        response: HttpServletResponse
    ): AuthResponseV1 {
        if (userRepository.findByUsername(request.username) != null) {
            throw AuthenticationException("User already registered", RequestError.Authentication.USERNAME_OCCUPIED)
        }

        val entity = UserEntity(
            username = request.username,
            email = request.username,
            name = request.displayName,
            passwordHash = passwordEncoder.encode(request.password),
            roles = mutableListOf(Roles.USER.name)
        )

        val user = userRepository.save(entity)
        val accessToken = jwtUtil.generateAccessToken(user.username)
        val refreshToken = jwtUtil.generateRefreshToken(user.username)
        response.addJwtCookie(accessToken)

        if (user.uid != null) {
            return userMapperV1.toAuthResponse(user, accessToken, refreshToken)
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
        request: SignInRequestV1,
        response: HttpServletResponse
    ): AuthResponseV1 {
        val auth = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (_: Exception) {
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

        return AuthResponseV1(
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
        request: RefreshRequestModelV1,
        response: HttpServletResponse
    ): RefreshResponseModelV1 {

        if (request.accessToken != null && jwtUtil.validate(request.accessToken)) {
            throw AuthenticationException("Jwt token is not expires", RequestError.Authentication.JWT_TOKEN_NOT_EXPIRED)
        }

        if (!jwtUtil.validate(request.refreshToken)) {
            throw AuthenticationException("Refresh token expires", RequestError.Authentication.REFRESH_TOKEN_EXPIRED)
        }

        val username = jwtUtil.getUsername(request.refreshToken)
        val accessToken = jwtUtil.generateAccessToken(username)
        response.addJwtCookie(accessToken)

        return RefreshResponseModelV1(
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
        request: NewPasswordRequestV1
    ) {
        val entity: ResetPasswordEntity = resetPasswordRepository.findByResetToken(token).orElseThrow {
            UserException(
                "Token expires or wrong",
                HttpStatus.BAD_REQUEST,
                RequestError.Authentication.RESET_TOKEN_NOT_FOUND
            )
        }
        entity.isUsed = true

        val user = entity.uid ?: throw UserException(
            "Reset token linked to a null user",
            RequestError.Authentication.RESET_TOKEN_LINKED_TO_NULL
        )
        val passwordHash = passwordEncoder.encode(/* rawPassword = */ request.password)
        user.passwordHash = passwordHash
    }

    /**
     * Method to verify Google id token
     * @return A [com.foodback.app.user.auth.dto.response.GoogleAuthResponseV1] with [com.foodback.app.user.auth.dto.response.GoogleAuthResponseV1.idToken] - ID of Google account.
     * If failed to verify Google ID token, return null
     */
    @Transactional
    fun verifyGoogleIdToken(googleIdToken: String): GoogleAuthResponseV1? {
        val transport = NetHttpTransport()
        val jsonFactory = GsonFactory()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(listOf(androidClientId, desktopClientId, webClientId))
            .build()

        val idToken = verifier.verify(googleIdToken) ?: return null
        val payload = idToken.payload

        return GoogleAuthResponseV1(
            idToken = payload.subject,
            email = payload.email,
            name = payload["name"] as String,
            picture = payload["picture"] as String,
            isEmailVerified = payload.emailVerified
        )
    }

    /**
     * Method to authenticate user with Google account. In first, try to find [GoogleAuthResponseV1.idToken] in [UserEntity].
     * If user found, update [UserEntity.googleId] and return [AuthResponseV1].
     * Else try to find user by email. If User found, update [UserEntity.googleId] and return [AuthResponseV1]
     * Otherwise, if user not found by [UserEntity.googleId] or [UserEntity.email] create new user and
     * return [AuthResponseV1]
     * @return [AuthResponseV1] Response with authentication result
     */
    @Transactional
    fun loadUser(
        googleAuthResponseV1: GoogleAuthResponseV1,
        response: HttpServletResponse
    ): AuthResponseV1 {
        val userByGoogle = userRepository.findByGoogleId(googleAuthResponseV1.idToken)
        if (userByGoogle.isPresent) {
            userByGoogle.get().apply {
                this.googleId = googleAuthResponseV1.idToken
            }
            return userByGoogle.get().setUserData(response)
        }

        val userByEmail = userRepository.findByEmail(googleAuthResponseV1.email)
        if (userByEmail.isPresent) {
            userByEmail.get().apply {
                this.googleId = googleAuthResponseV1.idToken
            }
            return userByEmail.get().setUserData(response)
        }

        val newUser = UserEntity(
            username = googleAuthResponseV1.email,
            passwordHash = null,
            email = googleAuthResponseV1.email,
            name = googleAuthResponseV1.name,
            photoUrl = googleAuthResponseV1.picture,
            roles = mutableListOf(Roles.USER.name),
            googleId = googleAuthResponseV1.idToken
        )
        return userRepository.save(newUser).setUserData(response)
    }

    /**
     * Method to create JWT and Access tokens and set it to Response Cookie
     * @return [AuthResponseV1] Response with JWT and Access tokens.
     */
    @Transactional
    private fun UserEntity.setUserData(response: HttpServletResponse): AuthResponseV1 {
        val accessToken = jwtUtil.generateAccessToken(this.username)
        val refreshToken = jwtUtil.generateRefreshToken(this.username)
        response.addJwtCookie(accessToken)

        return userMapperV1.toAuthResponse(
            userEntity = this,
            jwtToken = accessToken,
            refreshToken = refreshToken
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