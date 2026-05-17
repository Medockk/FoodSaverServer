package com.foodback.feature.users.impl.controller

import com.foodback.core.coreCommon.api.error.GlobalErrorResponse
import com.foodback.core.coreSecurity.api.service.JwtService
import com.foodback.feature.users.api.dto.auth.AuthResponse
import com.foodback.feature.users.api.dto.auth.GoogleRequest
import com.foodback.feature.users.api.dto.auth.LoginRequest
import com.foodback.feature.users.api.dto.auth.SignupRequest
import com.foodback.feature.users.api.service.auth.AuthGoogleService
import com.foodback.feature.users.api.service.auth.AuthService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
internal class AuthController(
    private val authService: AuthService,
    private val authGoogleService: AuthGoogleService,

    @Value($$"${jwt.expiration.ms}")
    private val maxJwtCookieAge: Int
) {

    @PostMapping("/login")
    fun login(
        @RequestBody
        request: LoginRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val result = authService.login(request)
        httpServletResponse.addJwtCookie(result.accessToken)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/signup")
    fun signup(
        @RequestBody
        request: SignupRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val result = authService.signup(request)
        httpServletResponse.addJwtCookie(result.accessToken)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/google")
    fun authenticateWithGoogle(
        @RequestBody
        request: GoogleRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val result = authGoogleService.authorizeWithGoogle(request)
        httpServletResponse.addJwtCookie(result.accessToken)
        return ResponseEntity.ok(result)
    }

    private fun HttpServletResponse.addJwtCookie(jwt: String) {
        val cookie = Cookie(JwtService.JWT_COOKIE_NAME, jwt)
        cookie.path = "/"
        cookie.maxAge = maxJwtCookieAge
        this.addCookie(cookie)
    }

    @PostMapping("/resetPassword/send")
    fun sendResetPassword(
        @RequestParam
        email: String
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}