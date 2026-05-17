package com.foodback.core.coreSecurity.impl.error

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

// diapason - 2000 < 3000
internal enum class SecurityErrorCode(override val code: Int): ServerErrorCode {

    UNAUTHORIZED_JWT_TOKEN(2001),
    UNAUTHORIZED_CSRF_TOKEN(2002),
}