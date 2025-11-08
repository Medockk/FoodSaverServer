package com.foodback.demo.security

import com.foodback.demo.entity.User.Roles
import com.foodback.demo.entity.User.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserDetailsImpl(
    private val userEntity: UserEntity
) : UserDetails {

    val uid: UUID = requireNotNull(userEntity.uid) {
        "User identifier must be not null"
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = userEntity.roles
            .mapNotNull { role ->
                try {
                    Roles.valueOf(role)
                } catch (_: Exception) {
                    null
                }
            }.flatMap { it.getAuthorities() }
            .toSet()

        return authorities
    }

    override fun getPassword(): String? {
        return userEntity.passwordHash
    }

    override fun getUsername(): String {
        return userEntity.username
    }
}