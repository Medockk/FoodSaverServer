package com.foodback.core.coreSecurity.impl.jwt

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipalSource
import com.foodback.core.coreSecurity.api.service.JwtService
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
internal class JwtAuthenticationFilter(
    private val jwtUtil: JwtService,
    private val userDetailsService: SecurityPrincipalSource
) : OncePerRequestFilter() {

    /**
     * If request have header Authorization Bearer jwt OR Cookie with jwt,
     * and this jwt token successfully verified, request will proceed,
     * overrise this request will cause 401 Unauthorized Exception
     * @throws Exception If JWT token not found
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val idToken = exactToken(request) ?: run {
            // если токена нет мы пропускаем запрос дальше
            println("JWT token doesn't exist. Continue filter chain")
            filterChain.doFilter(request, response)
            return
        }

        try {
            if (jwtUtil.validateToken(idToken)) {
                println("JWT token is valid")
                val username = jwtUtil.getUsername(idToken)
                val user = userDetailsService.findByUsername(username)
                    ?: throw Exception("User not found")

                val auth = UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.authorities
                )
                auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = auth

            }
            // если токен невалиден то не выбрасываем исключение, т.к. SecurityConfig находится последним (?)
            // в цепочке вызовов безопасность -> если выбросить исключение, то цепочка даже не дойдёт
            // до SecurityConfig -> JwtFilter даже на публичных путях не будет работать!!!
        } catch (e: Exception) {
            // то же самое
            println("JWT exception...")
            e.printStackTrace()
        }

        println("Checking JWT token is ending")
        filterChain.doFilter(request, response)
    }

    /**
     * Method to exact JWT token from request
     */
    private fun exactToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.removePrefix("Bearer ").trim()
            return token
        }

        val cookie = request.cookies ?: return null
        return cookie.firstOrNull { it.name == JwtService.JWT_COOKIE_NAME }?.value?.trim()
    }
}