package com.foodback.exception.general.Error

import com.foodback.exception.general.ErrorCode.ErrorCode
import org.springframework.http.HttpStatus

/**
 * Abstract class to define all exceptions
 * @param message Message of current exception
 * @property httpStatus Common HTTP status
 * @property customCode Custom HTTP status code
 * @property getHttpStandardCode General method to parse [httpStatus] to int code
 * @property getCustomCode General method to parse [customCode] to int value
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

    override fun getLocalizedMessage(): String {
        return message
    }
}