package com.foodback.core.coreSecurity.api.dto

interface SecurityPrincipalSource {
    fun findByUsername(username: String): SecurityPrincipal?
}