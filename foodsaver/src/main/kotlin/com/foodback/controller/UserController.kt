package com.foodback.controller

import com.foodback.dto.request.user.UserRequestModel
import com.foodback.dto.response.user.UserResponseModel
import com.foodback.exception.auth.UserException
import com.foodback.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

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
        principal: Principal
    ): ResponseEntity<UserResponseModel> {

        val uid = try {
            UUID.fromString(principal.name)
        } catch (_: Exception) {
            throw UserException(principal.name)
        }

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
        principal: Principal
    ): ResponseEntity<UserResponseModel> {
        val uid = try {
            UUID.fromString(principal.name)
        } catch (_: Exception) {
            throw UserException("Oops... Failed to get user data")
        }
        return ResponseEntity.ok(userService.updateUser(uid, request))
    }
}