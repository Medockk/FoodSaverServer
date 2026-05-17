package com.foodback.feature.featureProduct.impl.exception

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCode

/**
 * Diapason between 12_000 and 12_999
 */
internal enum class ProductErrorCode(override val code: Int): ServerErrorCode {

    PRODUCT_NOT_FOUND(12_001)
}

