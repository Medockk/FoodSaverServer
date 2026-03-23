package com.foodback.controller

import com.foodback.dto.request.auth.*
import com.foodback.dto.response.auth.AuthResponse
import com.foodback.dto.response.auth.RefreshResponseModel
import com.foodback.exception.auth.AuthenticationException
import com.foodback.exception.auth.UserException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.service.AuthService
import com.foodback.service.DefaultEmailService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller to authenticate users and update jwt token
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val defaultEmailService: DefaultEmailService
) {

    /**
     * Method to register user
     * @param signUpRequest Request body to register user.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [AuthResponse] Response of registration.
     */
    @PostMapping("signUp")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val response = authService.signUp(signUpRequest, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to login user
     * @param signInRequest Request body to register user.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [AuthResponse] - Response of authentication.
     */
    @PostMapping("signIn")
    fun signIn(
        @RequestBody signInRequest: SignInRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        println(signInRequest)
        val response = authService.signIn(signInRequest, httpServletResponse)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to refresh jwt token
     * @param refreshRequestModel Request body to refresh jwt token.
     * @param httpServletResponse Auto generated parameter. This param show any data, like cookies, headers ect.
     * @return [RefreshResponseModel] - Response of refresh jwt token.
     */
    @PostMapping("refresh")
    fun refreshToken(
        @RequestBody refreshRequestModel: RefreshRequestModel,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<RefreshResponseModel> {
        val response = authService.refreshToken(refreshRequestModel, httpServletResponse)
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
        request: ResetPasswordRequest
    ): ResponseEntity<Unit> {

        val email = request.email

        val resetToken = authService.resetPassword(email = email)
        defaultEmailService.sendMessage(email, resetToken)
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
        request: NewPasswordRequest,
        @RequestParam(required = true) id: UUID
    ): ResponseEntity<Unit> {

        if (request.password != request.confirmPassword)
            throw UserException("Password and Confirm password must be equals!", HttpStatus.BAD_REQUEST, RequestError.Authentication.PASSWORD_NOT_EQUALS)

        authService.resetPassword(token = id, request)
        return ResponseEntity.ok().build()
    }

    /**
     * Method to authenticate with Google Account
     * @param googleAuthRequest Request body, contained [GoogleAuthRequest.idToken] - Special id token of current
     * google account
     * @return A [ResponseEntity] of [AuthResponse]
     * @throws AuthenticationException If google id token failed to verify
     */
    @PostMapping("google")
    fun authenticateWithGoogle(
        @RequestBody
        googleAuthRequest: GoogleAuthRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val googleAuthResponse = authService.verifyGoogleIdToken(googleAuthRequest.idToken)
            ?: throw AuthenticationException(
                message = "Failed to verify Google id ${googleAuthRequest.idToken}",
                customCode = RequestError.Authentication.FAILED_TO_VERIFY_GOOGLE_ID,
                httpStatus = HttpStatus.BAD_REQUEST
            )

        val user = authService.loadUser(googleAuthResponse, response)

        return ResponseEntity.ok(user)
    }
}