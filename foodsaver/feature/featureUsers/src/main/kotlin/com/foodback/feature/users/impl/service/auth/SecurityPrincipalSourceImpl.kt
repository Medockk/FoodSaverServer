package com.foodback.feature.users.impl.service.auth

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.core.coreSecurity.api.dto.SecurityPrincipalSource
import com.foodback.core.coreSecurity.api.dto.UserRole
import com.foodback.feature.users.impl.repository.AuthRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class SecurityPrincipalSourceImpl(
    private val authRepository: AuthRepository
): SecurityPrincipalSource {

    override fun findByUsername(username: String): SecurityPrincipal? {
        val entity = authRepository
            .findByUsername(username)
            ?: return null

        return object : SecurityPrincipal {
            override val uid: UUID = entity.uid!!
            override val restaurantId: UUID? = entity.restaurantId

            override fun getAuthorities(): Collection<GrantedAuthority> {
                return entity.roles.flatMap { role ->
                    try {
                        role.getAllAuthorities()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        listOf(SimpleGrantedAuthority("ROLE_${UserRole.GUEST.name}"))
                    }
                }.toSet()
            }

            override fun getPassword(): String? {
                return entity.passwordHash
            }

            override fun getUsername(): String {
                return entity.username
            }
        }
    }
}