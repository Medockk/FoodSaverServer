package com.foodback.core.coreSecurity.impl.error

import com.foodback.core.coreCommon.api.error.GlobalErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

/**
 * Custom handler for 403 HTTP-error
 */
@Component
internal class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val statusCode = HttpStatus.FORBIDDEN.value()
        val globalErrorResponse = GlobalErrorResponse(
            error = "Access Denied! ${accessDeniedException.message}",
            message = "Oops...You don't have access to this resource.",
            httpCode = statusCode,
            serverErrorCode = SecurityErrorCode.UNAUTHORIZED_CSRF_TOKEN.code
        )

        response.status = statusCode
        response.contentType = "application/json"
        response.writer?.write(globalErrorResponse.toJson())
    }
}