package com.foodback.core.coreSecurity.impl.error

import com.foodback.core.coreCommon.api.error.GlobalErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * Custom handler for 401 HTTP-error
 */
@Component
internal class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val statusCode = HttpServletResponse.SC_UNAUTHORIZED

        val globalErrorResponse = GlobalErrorResponse(
            error = "Unauthorized! ${authException.message}",
            message = "Oops... authenticate in first to get access to this resource",
            httpCode = statusCode,
            serverErrorCode = SecurityErrorCode.UNAUTHORIZED_JWT_TOKEN.code
        )

        response.status = statusCode
        response.contentType = "application/json"
        response.writer.write(globalErrorResponse.toJson())
    }
}
