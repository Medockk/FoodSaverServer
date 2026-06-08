package com.foodback.feature.notification.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.notification.api.dto.UpdateFirebaseTokenRequest
import com.foodback.feature.notification.impl.service.UserPushTokenService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/firebase/token")
internal class FirebaseTokenController(
    private val userPushTokenService: UserPushTokenService
) {

    @PutMapping("/update")
    fun updateFirebaseToken(
        @RequestBody request: UpdateFirebaseTokenRequest,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ) {
        println("Update token \n${request.token}\n")
        userPushTokenService.updateFirebaseToken(
            principal.uid,
            request.token
        )
    }
}