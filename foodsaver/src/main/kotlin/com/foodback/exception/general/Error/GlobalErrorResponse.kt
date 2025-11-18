package com.foodback.exception.general.Error

/**
 * Global Error Response body
 * @param error Current Error Type
 * @param message Message of current error
 * @param httpCode Common HTTP response code of error
 * @param errorCode Custom HTTP response code of error
 * @param timestamp Time, when current error was called
 */
data class GlobalErrorResponse(
    val error: String,
    val message: String,
    val httpCode: Int,
    val errorCode: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
)
