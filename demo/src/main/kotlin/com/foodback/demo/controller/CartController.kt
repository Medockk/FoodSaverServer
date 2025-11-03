package com.foodback.demo.controller

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {

    @GetMapping
    fun getUserCart(
        principal: Principal
    ): ResponseEntity<List<ProductResponseModel>> {

        val uid = principal.name
        return ResponseEntity.ok(cartService.getUserCart(uid))
    }

    @PostMapping
    fun addProductToCart(
        @RequestBody(required = true)
        cartRequestModel: CartRequestModel,
        principal: Principal
    ) {
        val uid = principal.name

        cartService.addProductToCart(cartRequestModel, uid)
    }
}