package com.foodback.app.user.controller

import com.foodback.app.user.dto.request.UserRequestModelV1
import com.foodback.app.user.dto.response.UserResponseModelV1
import com.foodback.app.user.mapper.UserMapperV1
import com.foodback.exception.auth.UserException
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.app.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * Special controller to handle HTTP-requests to endpoint /api/user/
 * @param userService Service to do some server logic
 */
@RestController
@RequestMapping("/api/v1/user")
class UserControllerV1(
    private val userService: UserService,
    private val userMapperV1: UserMapperV1
) {

    /**
     * Method to get [UserResponseModelV1] - data about current user
     * @param principal auto-generated param, contains special UID
     */
    @Throws(UserException::class)
    @GetMapping
    fun getUser(
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<UserResponseModelV1> {

        val uid = principal.uid

        val result = userService.getUser(uid)
        println("USER IS ${result.photoUrl}")
        return ResponseEntity.ok(userMapperV1.mapToResponse(result))
    }

    /**
     * Method to update user data
     * @param request data to update user information
     * @param principal auto-generated param, contains special UID
     */
    @Throws(UserException::class)
    @PutMapping
    fun updateUser(
        @Valid
        @RequestBody(required = true)
        request: UserRequestModelV1,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<UserResponseModelV1> {
        val uid = principal.uid

        val user = userService.updateUser(uid, request)
        return ResponseEntity.ok(userMapperV1.mapToResponse(user))
    }

    @PutMapping("upload-avatar")
    fun uploadAvatar(
        @RequestParam("avatar")
        file: MultipartFile,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<String> {
        val url = userService.uploadAvatar(file, principal.uid)
        return ResponseEntity.ok(url)
    }
}