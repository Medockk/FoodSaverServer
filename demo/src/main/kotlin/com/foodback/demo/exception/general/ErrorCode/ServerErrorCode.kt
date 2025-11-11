package com.foodback.demo.exception.general.ErrorCode

/**
 * Typealias of [ServerErrorCode] to identify all custom errors
 */
typealias ErrorCode = ServerErrorCode

/**
 * Main Exception interface to determine all custom exceptions
 * @property code code of current exception
 */
sealed interface ServerErrorCode {

    val code: Int
}