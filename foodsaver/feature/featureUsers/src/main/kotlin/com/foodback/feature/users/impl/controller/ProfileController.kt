package com.foodback.feature.users.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.api.service.profile.ReadProfileService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/profile")
internal class ProfileController(
    private val readProfileService: ReadProfileService
) {

    @GetMapping("me")
    fun getProfile(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<ProfileResponse> {
        val profile = readProfileService.getProfileById(principal.uid)
        return ResponseEntity.ok(profile)
    }

    @PostMapping("ava")
    fun uploadAvatar(
        @RequestParam
        file: MultipartFile,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}