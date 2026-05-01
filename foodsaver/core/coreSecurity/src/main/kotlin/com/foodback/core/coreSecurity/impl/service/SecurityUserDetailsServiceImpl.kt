package com.foodback.core.coreSecurity.impl.service

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipalSource
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
internal class SecurityUserDetailsServiceImpl(
    private val securityPrincipalSource: SecurityPrincipalSource
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return securityPrincipalSource.findByUsername(username)
            ?: throw Exception("User not found!")
    }
}