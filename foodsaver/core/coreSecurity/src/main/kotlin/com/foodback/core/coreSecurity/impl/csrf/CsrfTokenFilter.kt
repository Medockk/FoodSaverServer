package com.foodback.core.coreSecurity.impl.csrf

import com.foodback.core.coreSecurity.api.service.SecurityConfigurationCustomizer
import com.foodback.core.coreSecurity.impl.error.CustomAccessDeniedHandler
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
internal class CsrfTokenFilter(
    private val accessDeniedHandler: CustomAccessDeniedHandler,
    private val securityConfigurationCustomizers: List<SecurityConfigurationCustomizer>
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
        val isPublicPath = securityConfigurationCustomizers
            .flatMap {
                it.getPublicPaths()
            }.any {
                path.startsWith(it.replace("/**", ""))
            }
        println("CSRF-TOKEN-FILTER: isPublicPath: $isPublicPath")

        val safeMethods = listOf("GET", "HEAD", "OPTIONS", "TRACE")
        val isSafeMethods = safeMethods.contains(request.method)
        println("CSRF-TOKEN-FILTER: isSafeMethods: $isSafeMethods")


        if (!isPublicPath && !isSafeMethods) {
            val cookie = request.cookies?.find { it.name == cookieName } ?: run {
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

            if (cookie.value != header) {
                accessDeniedHandler.handle(request, response, AccessDeniedException("CSRF-token not equal"))
                return
            }

            response.generateCsrfToken(request)
        }

        filterChain.doFilter(request, response)
    }

    /**
     * Generate and add CSRF-token to  cookie
     * @return Random CSRF-token
     */
    private fun HttpServletResponse.generateCsrfToken(request: HttpServletRequest): String {
        val existingToken = request.cookies?.find { it.name == this@CsrfTokenFilter.cookieName }
            ?.value
        val token = existingToken ?: UUID.randomUUID().toString()
        val cookie = Cookie(cookieName, token).apply {
            isHttpOnly = false
            path = "/"
            maxAge = 3_600_600
            secure = false
        }
        this.addCookie(cookie)
        return token
    }
}