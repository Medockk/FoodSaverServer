package com.foodback.feature.users.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.address.api.dto.AddressResponse
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.api.dto.proifle.UpdateProfileRequest
import com.foodback.feature.users.api.service.profile.ReadProfileService
import com.foodback.feature.users.api.service.profile.WriteProfileService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/profile")
internal class ProfileController(
    private val readProfileService: ReadProfileService,
    private val writeProfileService: WriteProfileService
) {

    @GetMapping("/me")
    fun getProfile(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<ProfileResponse> {
        val profile = readProfileService.getProfileById(principal.uid)
        return ResponseEntity.ok(profile)
    }

    @PutMapping("/update")
    fun uploadAvatar(
        @RequestPart(required = false)
        file: MultipartFile?,
        @RequestPart
        request: UpdateProfileRequest,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<ProfileResponse> {
        val extension = file?.originalFilename?.substringAfterLast('.', "png")
        val avatarBytes = file?.bytes
        val response = writeProfileService.updateProfile(
            request = request,
            avatarBytes = avatarBytes,
            avatarExtension = extension,
            userId = principal.uid
        )

        return ResponseEntity.ok(response)
    }

    @GetMapping("/my/addresses")
    fun getUserAddresses(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<List<AddressResponse>> {
        val addresses = readProfileService.getProfileAddresses(principal.uid)
        return if (addresses.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(addresses)
    }

    @GetMapping("/my/address")
    fun getCurrentAddress(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<AddressResponse> {
        val address = readProfileService.getProfileAddress(principal.uid)
        return if (address == null) ResponseEntity.noContent().build()
        else ResponseEntity.ok(address)
    }
}