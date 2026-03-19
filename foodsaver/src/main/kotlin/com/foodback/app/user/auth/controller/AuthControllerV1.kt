package com.foodback.app.user.auth.controller

import com.foodback.app.user.auth.dto.request.*
import com.foodback.app.user.auth.dto.response.AuthResponseV1
import com.foodback.app.user.auth.dto.response.RefreshResponseModelV1
import com.foodback.app.user.auth.service.AuthService
import com.foodback.exception.auth.AuthenticationException
import com.foodback.exception.auth.UserException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.service.notification.EmailNotificationService
import com.foodback.service.notification.NotificationService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller to authenticate users and update jwt token
 */
@RestController
@RequestMapping("/api/v1/auth")
class AuthControllerV1(
    private val authService: AuthService,
    @Qualifier("emailNotificationService")
    private val notificationService: NotificationService
) {

    /**
     * Method to register user
     * @param signUpRequestV1 Request body to register user.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [AuthResponseV1] Response of registration.
     */
    @PostMapping("signUp")
    fun signUp(
        @RequestBody signUpRequestV1: SignUpRequestV1,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponseV1> {
        val response = authService.signUp(signUpRequestV1, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to login user
     * @param signInRequestV1 Request body to register user.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [AuthResponseV1] - Response of authentication.
     */
    @PostMapping("signIn")
    fun signIn(
        @RequestBody signInRequestV1: SignInRequestV1,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponseV1> {
        println(signInRequestV1)
        val response = authService.signIn(signInRequestV1, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to refresh jwt token
     * @param refreshRequestModelV1 Request body to refresh jwt token.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [RefreshResponseModelV1] - Response of refresh jwt token.
     */
    @PostMapping("refresh")
    fun refreshToken(
        @RequestBody refreshRequestModelV1: RefreshRequestModelV1,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<RefreshResponseModelV1> {
        val response = authService.refreshToken(refreshRequestModelV1, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to reset password
     * @param request Request to send RESET-TOKEN to email
     */
    @Transactional
    @PutMapping("reset-password", params = ["!id"])
    fun changePassword(
        @Valid
        @RequestBody(required = true)
        request: ResetPasswordRequestV1
    ): ResponseEntity<Unit> {

        val email = request.email

        val resetToken = authService.resetPassword(email = email)
        notificationService.sendNotification(recipient = email, message = resetToken.toString())
        return ResponseEntity.ok().build()
    }

    /**
     * Method to reset password
     * @param request Request to send RESET-TOKEN to email
     * @param id RESET-TOKEN to reset password
     * @throws UserException If [request] or [id] is null
     */
    @Transactional
    @PutMapping("reset-password", params = ["id"])
    fun changePassword(
        @Valid
        @RequestBody(required = true)
        request: NewPasswordRequestV1,
        @RequestParam(required = true) id: UUID
    ): ResponseEntity<Unit> {

        if (request.password != request.confirmPassword)
            throw UserException(
                "Password and Confirm password must be equals!",
                HttpStatus.BAD_REQUEST,
                RequestError.Authentication.PASSWORD_NOT_EQUALS
            )

        authService.resetPassword(token = id, request)
        return ResponseEntity.ok().build()
    }

    /**
     * Method to authenticate with Google Account
     * @param googleAuthRequestV1 Request body, contained [GoogleAuthRequestV1.idToken] - Special id token of current
     * google account
     * @return A [ResponseEntity] of [AuthResponseV1]
     * @throws AuthenticationException If google id token failed to verify
     */
    @PostMapping("google")
    fun authenticateWithGoogle(
        @RequestBody
        googleAuthRequestV1: GoogleAuthRequestV1,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponseV1> {
        val googleAuthResponse = authService.verifyGoogleIdToken(googleAuthRequestV1.idToken)
            ?: throw AuthenticationException(
                message = "Failed to verify Google id ${googleAuthRequestV1.idToken}",
                customCode = RequestError.Authentication.FAILED_TO_VERIFY_GOOGLE_ID,
                httpStatus = HttpStatus.BAD_REQUEST
            )

        val user = authService.loadUser(googleAuthResponse, response)

        return ResponseEntity.ok(user)
    }
}