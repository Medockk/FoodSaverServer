package com.foodback.demo.controller

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

/**
 * Rest controller to process HTTP request to get/modify user cart
 */
@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {

    /**
     * Method to return [List] of [ProductResponseModel]
     */
    @GetMapping
    fun getUserCart(
        principal: Principal
    ): ResponseEntity<List<ProductResponseModel>> {

        val uid = principal.name
        println(uid)
        return ResponseEntity.ok(cartService.getUserCart(uid))
    }

    /**
     * Method to add product to cart
     * @param cartRequestModel request with special product id
     * @param principal auto generated value-param
     * [Principal] - Special entity of current user
     */
    @PostMapping
    fun addProductToCart(
        @RequestBody(required = true)
        cartRequestModel: CartRequestModel,
        principal: Principal
    ) {
        val uid = principal.name


        println(uid)
        cartService.addProductToCart(cartRequestModel, uid)
    }
}