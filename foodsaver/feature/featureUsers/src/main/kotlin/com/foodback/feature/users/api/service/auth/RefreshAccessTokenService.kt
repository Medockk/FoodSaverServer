package com.foodback.feature.users.api.service.auth

import com.foodback.feature.users.api.dto.auth.RefreshJwtTokenRequest

interface RefreshAccessTokenService {

    fun refreshJwtToken(request: RefreshJwtTokenRequest): String
}