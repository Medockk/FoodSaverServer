package com.foodback

import jakarta.servlet.ServletContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.get
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//class FoodSaverApplicationTests {
//
//    var mockMvc: MockMvc = object : MockMvc() {
//
//    }
//
//    @Test
//    fun contextLoads() {
//        mockMvc
//            .get("test")
//            .andExpect {
//                content {
//                    string("TEST")
//                }
//            }
//    }
//
//}
