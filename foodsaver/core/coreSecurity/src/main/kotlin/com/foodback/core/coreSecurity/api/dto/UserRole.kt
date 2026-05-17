package com.foodback.core.coreSecurity.api.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class UserRole(val permissions: Set<Permission>) {

    USER(emptySet()),
    GUEST(emptySet()),
    ADMIN(setOf(
        Permission.DELETE_RESTAURANT,
        Permission.ADD_RESTAURANT,
        Permission.DELETE_PRODUCT,
        Permission.ADD_CATEGORY
    )),
    MANAGER(setOf(
        Permission.ADD_PRODUCT,
        Permission.DELETE_PRODUCT,
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