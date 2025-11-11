package com.foodback.demo.exception.general.Error

import com.foodback.demo.exception.general.ErrorCode.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Abstract class to define all exceptions
 */
abstract class GlobalError(override val message: String) : Exception(message) {

    abstract val httpStatus: HttpStatus
    abstract val customCode: ErrorCode

    open fun getHttpStandardCode(): Int {
        return httpStatus.value()
    }

    open fun getCustomCode(): Int {
        return customCode.code
    }
}