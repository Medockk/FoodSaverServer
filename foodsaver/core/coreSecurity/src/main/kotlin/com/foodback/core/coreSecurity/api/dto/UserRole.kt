package com.foodback.core.coreSecurity.api.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class UserRole(val permissions: Set<Permission>) {

    USER(emptySet()),
    ADMIN(setOf(
        Permission.DELETE_RESTAURANT,
        Permission.DELETE_PRODUCT,
    )),
    MANAGER(setOf(
        Permission.EDIT_PRODUCT,
        Permission.EDIT_RESTAURANT
    ));

    fun getAllAuthorities(): List<GrantedAuthority> {
        val authorities = permissions.map {
            SimpleGrantedAuthority(it.name)
        }.toMutableList()
        authorities.add(SimpleGrantedAuthority(this.name))
        return authorities
    }
}