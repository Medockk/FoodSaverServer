package com.foodback.feature.users.impl.controller

import com.foodback.core.coreSecurity.api.service.JwtService
import com.foodback.feature.users.api.dto.AuthResponse
import com.foodback.feature.users.api.dto.GoogleRequest
import com.foodback.feature.users.api.dto.LoginRequest
import com.foodback.feature.users.api.dto.SignupRequest
import com.foodback.feature.users.api.service.AuthGoogleService
import com.foodback.feature.users.api.service.AuthService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
internal class AuthController(
    private val authService: AuthService,
    private val authGoogleService: AuthGoogleService
) {

    @PostMapping("/login")
    fun login(
        @RequestBody
        request: LoginRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val result = authService.login(request)
        val cookie = Cookie(JwtService.JWT_COOKIE_NAME, result.accessToken)
        httpServletResponse.addCookie(cookie)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/signup")
    fun signup(
        @RequestBody
        request: SignupRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val result = authService.signup(request)
        val cookie = Cookie(JwtService.JWT_COOKIE_NAME, result.accessToken)
        httpServletResponse.addCookie(cookie)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/google")
    fun authenticateWithGoogle(
        @RequestBody
        request: GoogleRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val result = authGoogleService.authorizeWithGoogle(request)
        val cookie = Cookie(JwtService.JWT_COOKIE_NAME, result.accessToken)
        return ResponseEntity.ok(result)
    }
}