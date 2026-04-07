package com.foodback.app.firebase.controller

import com.foodback.app.firebase.service.FCMTokensService
import com.foodback.security.auth.UserDetailsImpl
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/fcm")
class FCMTokensController(
    private val fcmTokensService: FCMTokensService
) {

    @PostMapping
    fun saveToken(
        @RequestParam
        token: String,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ) {
        println("\n\n\nFCM TOKEN\n\n\n")
        fcmTokensService.saveToken(token, principal.uid)
    }
}