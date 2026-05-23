package com.foodback.feature.address.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

/**
 * error code between 16_001 and 16_999
 */
enum class AddressErrorCode(override val code: Int): ServerErrorCode {

    ADDRESS_NOT_FOUND(16_001)
}