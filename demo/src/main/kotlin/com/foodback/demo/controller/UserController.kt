package com.foodback.demo.controller

import com.foodback.demo.dto.request.user.UserRequestModel
import com.foodback.demo.dto.response.user.UserResponseModel
import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.exception.general.ErrorCode.RequestError
import com.foodback.demo.service.AuthService
import com.foodback.demo.service.DefaultEmailService
import com.foodback.demo.service.UserService
import com.foodback.demo.utils.toUUID
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val defaultEmailService: DefaultEmailService,

    private val authService: AuthService
) {

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

    @Transactional
    @PutMapping("reset-password")
    fun changePassword(
        principal: Principal,
    ): ResponseEntity<Unit> {
        val uid = principal.name.toUUID()
        val user = userService.getUser(uid)
        val email = user.email

        if (email.isNullOrBlank())
            throw UserException("Email must be not blank", RequestError.UserRequest.EMPTY_EMAIL)

        val resetToken = authService.resetPassword(email = email)
        defaultEmailService.sendMassage(email, resetToken)

        return ResponseEntity.ok().build()
    }
}