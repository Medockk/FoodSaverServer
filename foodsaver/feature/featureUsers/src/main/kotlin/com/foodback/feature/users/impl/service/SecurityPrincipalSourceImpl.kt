package com.foodback.feature.users.impl.service

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.core.coreSecurity.api.dto.SecurityPrincipalSource
import com.foodback.feature.users.impl.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class SecurityPrincipalSourceImpl(
    private val userRepository: UserRepository
): SecurityPrincipalSource {

    override fun findByUsername(username: String): SecurityPrincipal? {
        val entity = userRepository
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
                        listOf(SimpleGrantedAuthority("ROLE_GUEST"))
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