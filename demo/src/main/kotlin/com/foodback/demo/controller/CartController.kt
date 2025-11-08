package com.foodback.demo.controller

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.service.CartService
import com.foodback.demo.utils.toUUID
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

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
        val uid = principal.name.toUUID()
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
        val uid = principal.name.toUUID()
        cartService.addProductToCart(cartRequestModel, uid)
    }

    @DeleteMapping
    fun deleteProduct(
        @RequestParam(value = "product_id", required = true)
        productId: UUID,
        principal: Principal
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(
            cartService.deleteProductById(productId, principal.name.toUUID())
        )
    }

    @DeleteMapping("all")
    fun clearCart(
        principal: Principal
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(
            cartService.clearCart(principal.name.toUUID())
        )
    }

    @PutMapping("increase")
    fun increaseProductCount(
        @RequestBody
        request: CartRequestModel,
        principal: Principal
    ): ResponseEntity<ProductResponseModel> {
        val uid = principal.name.toUUID()
        val product = cartService.increaseProduct(request, uid)
        return ResponseEntity.ok(product)
    }

    @PutMapping("decrease")
    fun decreaseProductCount(
        @RequestBody
        request: CartRequestModel,
        principal: Principal
    ): ResponseEntity<ProductResponseModel> {
        val uid = principal.name.toUUID()
        val product = cartService.decreaseProduct(request, uid)

        return ResponseEntity.ok(product)
    }
}