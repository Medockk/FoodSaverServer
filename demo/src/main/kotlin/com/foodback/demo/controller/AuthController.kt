package com.foodback.demo.controller

import com.foodback.demo.dto.request.auth.*
import com.foodback.demo.dto.response.auth.AuthResponse
import com.foodback.demo.dto.response.auth.RefreshResponseModel
import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.exception.general.ErrorCode.RequestError
import com.foodback.demo.service.AuthService
import com.foodback.demo.service.DefaultEmailService
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
}

//@PostMapping("image")
//fun uploadImage(
//    @RequestParam("file") file: MultipartFile,
//    httpServletRequest: HttpServletRequest
//) {
//    if (file.isEmpty) {
//        throw Exception("File is empty!")
//    }
//
//    val directory = System.getProperty("user.dir") + "/spring_images"
//    File(directory).mkdirs()
//    val time = System.currentTimeMillis()
//
//    val name = file.originalFilename ?: file.name
//    val originName = name.dropLastWhile { it != '.' }.dropLast(1)
//    val suffix = name.replaceBeforeLast(
//        ".",
//        "_$time"
//    )
//    try {
//        val bytes = file.bytes
//        val path = Paths.get(
//            "$directory/$originName$suffix"
//        )
//        Files.write(path, bytes)
//        path.toFile().setLastModified(time)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        throw e
//    }
//
//    val jwt = httpServletRequest.cookies.find { it.name == "jwt" }?.value ?: return
//    val uid = FirebaseAuth.getInstance().verifyIdToken(jwt).uid
//    authService.getUsers()
//    supabaseService.uploadAvatar(file, "$originName$suffix", uid)
//}
