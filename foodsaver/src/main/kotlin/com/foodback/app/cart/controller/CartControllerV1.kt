package com.foodback.app.cart.controller

import com.foodback.app.cart.dto.request.CartRequestModel
import com.foodback.app.cart.dto.response.CartResponseModel
import com.foodback.app.cart.mapper.CartMapperV1
import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.app.cart.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Rest controller to process HTTP request to get/modify user cart
 */
@RestController
@RequestMapping("/api/v1/cart")
class CartControllerV1(
    private val cartService: CartService,
    private val cartMapperV1: CartMapperV1
) {

    /**
     * Method to return [List] of [ProductResponseModel]
     */
    @GetMapping
    fun getUserCart(
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<List<CartResponseModel>> {
        val uid = principal.uid
        return ResponseEntity.ok(cartService.getUserCart(uid))
    }

    /**
     * Method to add product to cart
     * @param cartRequestModel request with special product id
     * @param principal auto generated value-param
     * [java.security.Principal] - Special entity of current user
     */
    @PostMapping
    fun addProductToCart(
        @RequestBody(required = true)
        cartRequestModel: CartRequestModel,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<CartResponseModel> {
        val uid = principal.uid
        val cartItemEntity = cartService.addProductToCart(cartRequestModel, uid)

        return if (cartItemEntity == null) {
            ResponseEntity.badRequest().build()
        } else {
            ResponseEntity.ok(cartMapperV1.mapToCartResponse(cartItemEntity))
        }
    }

    /**
     * Method to delete product from cart
     * @param productId special product id
     * @param principal auto-generated param, contains special UID
     */
    @DeleteMapping
    fun deleteProduct(
        @RequestParam(value = "product_id", required = true)
        productId: UUID,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<Unit> {
        val uid = principal.uid
        cartService.deleteProductById(productId, uid)
        return ResponseEntity.ok().build()
    }

    /**
     * Method to clear user cart
     * @param principal auto-generated param, contains special UID
     */
    @DeleteMapping("all")
    fun clearCart(
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<Unit> {
        cartService.clearCart(principal.uid)
        return ResponseEntity.ok().build()
    }

    /**
     * Method to increase product count
     * @param request request, contains product id and quantity
     * @param principal auto-generated param, contains special UID
     */
    @PutMapping("increase")
    fun increaseProductCount(
        @RequestBody
        request: CartRequestModel,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<ProductResponseModel> {
        val uid = principal.uid
        val product = cartService.increaseProduct(request, uid)
        return ResponseEntity.ok(product)
    }

    /**
     * Method to decrease product count
     * @param request request, contains product id and quantity
     * @param principal auto-generated param, contains special UID
     */
    @PutMapping("decrease")
    fun decreaseProductCount(
        @RequestBody
        request: CartRequestModel,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<ProductResponseModel> {
        val uid = principal.uid
        val product = cartService.decreaseProduct(request, uid)

        return ResponseEntity.ok(product)
    }
}