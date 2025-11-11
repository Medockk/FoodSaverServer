package com.foodback.demo.utils

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component

/**
 * Utility to create JWT Token and exact JWT token from Cookies
 */
@Component
class CookieUtil {

    /**
     * Method to add JWT token to cookie
     * @return A [Cookie] with [jwt]
     */
    fun createJwtCookie(jwt: String, name: String = "jwt"): Cookie {
        return Cookie(name, jwt).apply {
            path = "/"
            maxAge = 3600
            isHttpOnly = true
            secure = false
        }
    }
}
