package com.foodback.exception.handler

import com.foodback.exception.general.Error.GlobalErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

/**
 * Custom handler for 401 HTTP-error
 */
@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val statusCode = HttpStatus.UNAUTHORIZED.value()
        val globalErrorResponse = GlobalErrorResponse(
            error = "Unauthorized",
            message = "Oops... authenticate in first to get access to this resource",
            httpCode = statusCode
        )

        response.status = statusCode
        response.contentType = "application/json"
        response.writer.write(globalErrorResponse.toJson())
    }
}

/**
 * Custom handler for 403 HTTP-error
 */
@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val statusCode = HttpStatus.FORBIDDEN.value()
        val globalErrorResponse = GlobalErrorResponse(
            error = "Access Denied",
            message = "Oops...You don't have access to this resource",
            httpCode = statusCode
        )

        response.status = statusCode
        response.contentType = "application/json"
        response.writer?.write(globalErrorResponse.toJson())
    }
}

private fun GlobalErrorResponse.toJson(): String {
    return """
            {
                "error": "$error",
                "message": "$message",
                "httpCode": "$httpCode",
                "timestamp": "$timestamp"
            }
        """.trimIndent()
}
