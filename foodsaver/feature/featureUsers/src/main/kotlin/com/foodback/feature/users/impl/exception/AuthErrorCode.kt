package com.foodback.feature.users.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

// diapason = 11_000 < 12_000
internal enum class AuthErrorCode(override val code: Int): ServerErrorCode {
    USER_NOT_FOUND(11_001),
    USER_ALREADY_REGISTERED(11_002),
    INVALID_GOOGLE_TOKEN(11_003),

    JWT_TOKEN_NOT_EXPIRED(11_011),
    REFRESH_TOKEN_EXPIRED(11_012),
}