package com.foodback.demo.exception.general.Error

/**
 * Global Error Response body
 * @param error Current Error Type
 * @param message Message of current error
 * @param code Code of error
 * @param timestamp Time, when current error was called
 */
data class GlobalErrorResponse(
    val error: String,
    val message: String,
    val httpCode: Int,
    val errorCode: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
)
