package com.foodback.demo.utils

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component

/**
 * Utility to create JWT Token and exact JWT token from Cookies
 */
@Component
class CookieUtil {

    fun createJwtCookie(jwt: String, name: String = "jwt"): Cookie {
        return Cookie(name, jwt).apply {
            path = "/"
            maxAge = 3600
            isHttpOnly = true
            secure = false
        }
    }
}
