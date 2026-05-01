package com.foodback.core.coreCommon.api

class PermittedRequests {
    companion object {
        val permittedRequestPaths = arrayOf(
            "/api/v1/auth",
            "/media",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html"
        )
    }
}