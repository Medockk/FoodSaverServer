package com.foodback.core.coreCommon.api.errorCode

// diapason = 1000 < 2000
enum class CommonErrorCode(override val code: Int): ServerErrorCode {
    LARGE_FILE(1001)
}