package com.foodback.entity.User

import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Enum class with all roles.
 * @param permissions Permissions of current role
 */
enum class Roles(val permissions: Set<String>) {
    USER(setOf(Permissions.REFRESH_TOKEN.value)),
    ADMIN(
        setOf(
            Permissions.GET_USERS.value,
            Permissions.REFRESH_TOKEN.value,
            Permissions.DELETE_PRODUCT.value,
        )
    );

    /**
     * Method to get [SimpleGrantedAuthority] - Authority of current User/Role
     */
    fun getAuthorities(): Set<SimpleGrantedAuthority> {
        val permissions = this.permissions.map {
            SimpleGrantedAuthority(it)
        }.toSet()

        val rolesAuthorities = SimpleGrantedAuthority("ROLE_$name")
        return permissions + rolesAuthorities
    }
}

/**
 * Enum class with all permissions
 */
enum class Permissions(val value: String) {
    REFRESH_TOKEN("REFRESH_TOKEN"),
    GET_USERS("GET_USERS"),
    DELETE_PRODUCT("DELETE_PRODUCT")
}