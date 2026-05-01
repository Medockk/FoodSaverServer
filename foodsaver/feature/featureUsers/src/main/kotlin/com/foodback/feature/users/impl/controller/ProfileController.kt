package com.foodback.feature.users.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/profile")
internal class ProfileController {

    @GetMapping
    fun getProfile(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ) {
        println("Principal is ${principal.username}")
    }
}