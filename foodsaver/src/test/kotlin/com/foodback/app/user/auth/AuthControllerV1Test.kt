package com.foodback.app.user.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.foodback.app.user.auth.dto.request.ResetPasswordRequestV1
import com.foodback.app.user.auth.dto.request.SignInRequestV1
import com.foodback.app.user.auth.dto.request.SignUpRequestV1
import com.foodback.app.user.auth.dto.response.AuthResponseV1
import com.foodback.app.user.auth.service.AuthService
import com.foodback.service.notification.NotificationService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerV1Test {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = jacksonObjectMapper()

    @MockitoBean
    private lateinit var authService: AuthService

    @MockitoBean(name = "emailNotificationService")
    private lateinit var notificationService: NotificationService

    @Test
    fun testSignUp_Success() {
        val request = SignUpRequestV1("testuser", "password123", "DisplayName")
        val response = AuthResponseV1(UUID.randomUUID(), "testuser", listOf("USER"), "at", "rt", 3600L)

        `when`(authService.signUp(any(), any())).thenReturn(response)

        mockMvc.perform(post("/api/v1/auth/signUp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
    }

    @Test
    fun testSignIn_Success() {
        val request = SignInRequestV1("testuser", "password123", true)
        val response = AuthResponseV1(UUID.randomUUID(), "testuser", listOf("USER"), "at", "rt", 3600L)

        `when`(authService.signIn(any(), any())).thenReturn(response)

        mockMvc.perform(post("/api/v1/auth/signIn")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
    }

    @Test
    fun testRequestResetPassword_Success() {
        val request = ResetPasswordRequestV1("test@mail.ru")
        `when`(authService.resetPassword(any<String>())).thenReturn(UUID.randomUUID())

        mockMvc.perform(put("/api/v1/auth/reset-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
    }
}