package com.foodback.feature.order.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

/**
 * [code] between 20_000 and 20_999
 */
internal enum class OrderErrorCode(override val code: Int): ServerErrorCode {

    QUANTITY_OUT_OF_BOUNDS(20_001)
}