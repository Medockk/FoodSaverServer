package com.foodback.controller

import com.foodback.dto.request.user.UserRequestModel
import com.foodback.dto.response.user.UserResponseModel
import com.foodback.exception.auth.UserException
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

/**
 * Special controller to handle HTTP-requests to endpoint /api/user/
 * @param userService Service to do some server logic
 */
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    /**
     * Method to get [UserResponseModel] - data about current user
     * @param principal auto-generated param, contains special UID
     */
    @Throws(UserException::class)
    @GetMapping
    fun getUser(
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<UserResponseModel> {

        val uid = principal.uid

        val result = userService.getUser(uid)
        return ResponseEntity.ok(result)
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
        request: UserRequestModel,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<UserResponseModel> {
        val uid = principal.uid
        return ResponseEntity.ok(userService.updateUser(uid, request))
    }
}