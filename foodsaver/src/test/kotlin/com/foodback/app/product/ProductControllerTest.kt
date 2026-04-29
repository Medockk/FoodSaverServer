package com.foodback.app.product

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.app.product.entity.ProductEntity
import com.foodback.app.product.mapper.ProductMapperV1
import com.foodback.app.product.service.PersonalizationService
import com.foodback.app.product.service.ProductService
import com.foodback.app.user.entity.UserEntity
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.security.auth.UserDetailsServiceImpl
import com.foodback.security.jwt.JwtUtil
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var productService: ProductService

    @MockitoBean
    private lateinit var personalizationService: PersonalizationService

    @MockitoBean
    private lateinit var productMapperV1: ProductMapperV1

    @MockitoBean
    private lateinit var jwtUtil: JwtUtil
    @MockitoBean
    private lateinit var userDetailsServiceImpl: UserDetailsServiceImpl

    private val objectMapper = jacksonObjectMapper()

    private lateinit var testUser: UserEntity
    private lateinit var userDetails: UserDetailsImpl

    @BeforeEach
    fun setup() {
        testUser = UserEntity(uid = UUID.randomUUID(), username = "tester")
        userDetails = UserDetailsImpl(testUser)
        `when`(userDetailsServiceImpl.loadUserByUsername("tester")).thenReturn(userDetails)

        `when`(jwtUtil.validate(any())).thenReturn(true)
        `when`(jwtUtil.getUsername(any())).thenReturn("tester")
    }

    @Test
    fun `getProducts with pagination and sorting should pass to service`() {
        val productId = UUID.randomUUID()
        val enterpriseId = UUID.randomUUID()

        val productEntity = ProductEntity(
            id = productId,
            title = "Milk",
            description = "Fresh milk",
            cost = 100f,
            unit = 1,
            unitName = "Liter",
            addedAt = Instant.now()
        )

        val responseModel = ProductResponseModel(
            productId = productId,
            title = "Milk",
            description = "Fresh milk",
            photoUrl = null,
            cost = 100f,
            costUnit = "Rub",
            count = 10,
            rating = 5f,
            categoryIds = emptyList(),
            enterpriseId = enterpriseId,
            unit = 1,
            unitName = "Liter"
        )

        `when`(personalizationService.personalizeProducts(any(), any())).thenReturn(listOf(productEntity))
        `when`(productMapperV1.mapToResponse(any())).thenReturn(responseModel)

        mockMvc.perform(get("/api/v1/products")
            .header("Authorization", "Bearer manual-test-token")
            .header("X-XSRF-TOKEN", "manual-test-csrf-token")
            .cookie(Cookie("XSRF-TOKEN", "manual-test-csrf-token"))
            .with(user(userDetails))
            .param("page", "1")
            .param("size", "20")
            .param("searchRadiusKm", "5.0")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].title").value("Milk"))
            .andExpect(jsonPath("$[0].productId").value(productId.toString()))
    }

    @Test
    fun `searchProduct should handle multiple categoryIds and pagination`() {
        val cat1 = UUID.randomUUID()
        val cat2 = UUID.randomUUID()

        `when`(productService.searchProducts(any(), any(), any())).thenReturn(emptyList())

        mockMvc.perform(get("/api/v1/products/search")
            .header("Authorization", "Bearer manual-test-token")
            .header("X-XSRF-TOKEN", "manual-test-csrf-token")
            .cookie(Cookie("XSRF-TOKEN", "manual-test-csrf-token"))
            .param("name", "pizza")
            .param("categoryIds", cat1.toString(), cat2.toString())
            .param("page", "0")
            .param("size", "5")
            .param("sort", "expiresAt,asc")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
    }

    @Test
    fun `getProducts should return 204`() {
        `when`(personalizationService.personalizeProducts(any(), any())).thenReturn(emptyList())

        mockMvc.perform(get("/api/v1/products")
            .header("Authorization", "Bearer manual-test-token")
            .header("X-XSRF-TOKEN", "manual-test-csrf-token")
            .cookie(Cookie("XSRF-TOKEN", "manual-test-csrf-token"))
            .with(user(userDetails))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
    }
}