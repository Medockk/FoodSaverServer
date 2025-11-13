package com.foodback.demo.security.csrf

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Special filter of CSRF-tokens. This filter check: does the current request have CSRF-token.
 * If it doesn't have: generate the new CSRF-token and save token in [csrfTokenRepository].
 * Otherwise, save token to [csrfTokenRepository], to make sure, that the token will be in Cookie
 * @param csrfTokenRepository Repository of CSRF-tokens to compare CSRF-tokens from Header and Cookie and generate new token
 */
class CsrfTokenFilter(
    private val csrfTokenRepository: CsrfTokenRepository
): OncePerRequestFilter() {

    /**
     * Method to do filter logic
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val csrfToken = request.getAttribute(CsrfToken::class.java.name) as? CsrfToken
        if (csrfToken != null) {
            csrfTokenRepository.saveToken(csrfToken, request, response)
            return
        }

        var tokenFromRepository = csrfTokenRepository.loadToken(request)
        if (tokenFromRepository == null) {
            tokenFromRepository = csrfTokenRepository.generateToken(request)
        }

        csrfTokenRepository.saveToken(tokenFromRepository, request, response)
        filterChain.doFilter(request, response)
    }
}