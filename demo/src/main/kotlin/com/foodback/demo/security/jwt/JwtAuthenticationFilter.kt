package com.foodback.demo.security.jwt

import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.security.auth.UserDetailsImpl
import com.foodback.demo.security.auth.UserDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Class to verify all request to server except /api/auth
 */
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    /**
     * Method for excluding the /api/auth endpoint from JWT token verification
     */
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return path.startsWith("/api/auth")
    }

    /**
     * If request have header Authorization Bearer jwt OR Cookie with jwt,
     * and this jwt token successfully verified, request will proceed,
     * overrise this request will cause 401 Unauthorized Exception
     * @throws UserException If user with this email doesn't have in database
     * @throws Exception If JWT token not found
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val idToken = exactToken(request) ?: run {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header")
            return
        }

        try {
            if (jwtUtil.validate(idToken)) {
                val username = jwtUtil.getUsername(idToken)
                val user = userDetailsService.loadUserByUsername(username) as UserDetailsImpl

                val auth = UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.authorities
                )
                auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = auth

                filterChain.doFilter(request, response)
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Method to exact JWT token from request
     */
    private fun exactToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }

        val cookie = request.cookies ?: return null
        println("\n\n\n${cookie.firstOrNull { it.name == "jwt" }?.value}\n\n\n")
        return cookie.firstOrNull { it.name == "jwt" }?.value
    }
}