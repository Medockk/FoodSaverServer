package com.foodback.feature.users.impl.exception.profile

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

/**
 * diapason between 13_000 and 13_999
 */
internal enum class ProfileErrorCode(override val code: Int): ServerErrorCode {

    PROFILE_NOT_FOUND(13_001)
}