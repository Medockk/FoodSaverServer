package com.foodback.feature.users.impl.controller

import com.foodback.core.coreSecurity.api.service.JwtService
import com.foodback.feature.users.api.dto.auth.RefreshJwtTokenRequest
import com.foodback.feature.users.api.service.auth.RefreshAccessTokenService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/refreshToken")
internal class RefreshAccessTokenController(
    private val refreshAccessTokenService: RefreshAccessTokenService
) {

    @PutMapping
    fun refreshAccessToken(
        @RequestBody request: RefreshJwtTokenRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<String> {
        val result = refreshAccessTokenService.refreshJwtToken(request)
        val cookie = Cookie(JwtService.JWT_COOKIE_NAME, result)
        httpServletResponse.addCookie(cookie)
        return ResponseEntity.ok(result)
    }
}