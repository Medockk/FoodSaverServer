package com.foodback.demo.security.auth

import com.foodback.demo.entity.User.Roles
import com.foodback.demo.entity.User.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

/**
 * A class, that returns [org.springframework.security.authentication.AuthenticationManager], when
 * authentication is Success
 */
class UserDetailsImpl(
    private val userEntity: UserEntity
) : UserDetails {

    val uid: UUID = requireNotNull(userEntity.uid) {
        "User identifier must be not null"
    }

    /**
     * Method to get all user authorize
     */
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

    /**
     * Method to get encoded password
     */
    override fun getPassword(): String? {
        return userEntity.passwordHash
    }

    /**
     * Method to get unique username
     */
    override fun getUsername(): String {
        return userEntity.username
    }
}