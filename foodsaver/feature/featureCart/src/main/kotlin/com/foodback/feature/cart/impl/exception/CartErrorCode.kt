package com.foodback.feature.cart.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

/**
 * diapason between 14_000 and 14_999
 */
internal enum class CartErrorCode(override val code: Int): ServerErrorCode {

    CART_NOT_FOUND(14_001),
    CART_ITEM_NOT_FOUND(14_002),
}