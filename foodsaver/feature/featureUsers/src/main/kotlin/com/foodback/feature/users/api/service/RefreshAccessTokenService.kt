package com.foodback.feature.users.api.service

import com.foodback.feature.users.api.dto.RefreshJwtTokenRequest

interface RefreshAccessTokenService {

    fun refreshJwtToken(request: RefreshJwtTokenRequest): String
}