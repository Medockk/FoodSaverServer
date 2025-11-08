package com.foodback.demo.controller

import com.foodback.demo.dto.request.user.UserRequestModel
import com.foodback.demo.dto.response.user.UserResponseModel
import com.foodback.demo.exception.auth.UserNotFoundException
import com.foodback.demo.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @Throws(UserNotFoundException::class)
    @GetMapping
    fun getUser(
        principal: Principal
    ): ResponseEntity<UserResponseModel> {

        val uid = try {
            UUID.fromString(principal.name)
        } catch (_: Exception) {
            throw UserNotFoundException(principal.name)
        }

        val result = userService.getUser(uid)
        return ResponseEntity.ok(result)
    }

    @Throws(UserNotFoundException::class)
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
            throw UserNotFoundException("Oops... Failed to get user data")
        }
        return ResponseEntity.ok(userService.updateUser(uid, request))
    }
}