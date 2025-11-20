package com.foodback.security.csrf

import com.foodback.exception.handler.CustomAccessDeniedHandler
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

/**
 * Special filter of CSRF-tokens. This filter check: does the current request have CSRF-token.
 */
class CsrfTokenFilter(
    private val accessDeniedHandler: CustomAccessDeniedHandler
) : OncePerRequestFilter() {

    private val cookieName = "XSRF-TOKEN"
    private val headerName = "X-XSRF-TOKEN"

    /**
     * Method to do filter logic
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val path = request.requestURI
        response.generateCsrfToken()
        if (!path.startsWith("/api/auth")) {
            val cookie = request.cookies.find { it.name == cookieName } ?: run {
                accessDeniedHandler.handle(
                    request,
                    response,
                    AccessDeniedException("Failed to exact CSRF-token from cookies")
                )
                return
            }
            val header = request.getHeader(headerName) ?: run {
                accessDeniedHandler.handle(
                    request,
                    response,
                    AccessDeniedException("Failed to exact CSRF-token from header")
                )
                return
            }

            if (cookie.value == header) {
                filterChain.doFilter(request, response)
            } else {
                accessDeniedHandler.handle(request, response, AccessDeniedException("CSRF-token not equal"))
                return
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    /**
     * Generate and add CSRF-token to  cookie
     * @return Random CSRF-token
     */
    private fun HttpServletResponse.generateCsrfToken(): UUID {
        val token = UUID.randomUUID()
        val cookie = Cookie(cookieName, token.toString()).apply {
            this.isHttpOnly = false
            this.path = "/"
        }
        this.addCookie(cookie)
        return token
    }
}