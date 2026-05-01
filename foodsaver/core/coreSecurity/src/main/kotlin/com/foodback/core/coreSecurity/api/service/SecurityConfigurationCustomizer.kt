package com.foodback.core.coreSecurity.api.service

interface SecurityConfigurationCustomizer {

    fun getPublicPaths(): List<String>
}