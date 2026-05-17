package com.foodback.feature.users.impl.service.auth

import com.foodback.core.coreSecurity.api.service.JwtService
import com.foodback.feature.users.api.dto.auth.RefreshJwtTokenRequest
import com.foodback.feature.users.api.service.auth.RefreshAccessTokenService
import com.foodback.feature.users.impl.exception.auth.RefreshTokenExpiredException
import com.foodback.feature.users.impl.exception.auth.TokenNotExpiredException
import org.springframework.stereotype.Service

@Service
internal class RefreshAccessTokenServiceImpl(
    private val jwtService: JwtService
): RefreshAccessTokenService {

    override fun refreshJwtToken(request: RefreshJwtTokenRequest): String {
        if (jwtService.validateToken(request.accessToken)) {
            throw TokenNotExpiredException()
        }

        if (!jwtService.validateToken(request.refreshToken)) {
            throw RefreshTokenExpiredException()
        }

        val username = jwtService.getUsername(request.refreshToken)
        val newJwtToken = jwtService.generateAccessToken(username)

        return newJwtToken
    }
}