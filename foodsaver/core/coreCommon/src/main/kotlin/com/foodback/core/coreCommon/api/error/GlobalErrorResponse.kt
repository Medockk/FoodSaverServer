package com.foodback.core.coreCommon.api.error

/**
 * Global Error Response body
 * @param error Current Error Type
 * @param message Message of current error
 * @param httpCode Common HTTP response code of error
 * @param serverErrorCode Custom HTTP response code of error
 * @param timestamp Time, when current error was called
 */
data class GlobalErrorResponse(
    val error: String,
    val message: String,
    val httpCode: Int,
    val serverErrorCode: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
) {
    fun toJson(): String {
        return """
            {
                "error": "$error",
                "message": "$message",
                "httpCode": "$httpCode",
                "serverErrorCode": "$serverErrorCode",
                "timestamp": "$timestamp"
            }
        """.trimIndent()
    }
}