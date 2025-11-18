package com.foodback.controller

import com.foodback.dto.request.cart.CartRequestModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<List<ProductResponseModel>> {
        val uid = principal.uid
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
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ) {
        val uid = principal.uid
        cartService.addProductToCart(cartRequestModel, uid)
    }

    /**
     * Method to delete product, if user have role ADMIN, of else throw [AccessDeniedException]
     * @param productId special product id
     * @param principal auto-generated param, contains special UID
     */
    @DeleteMapping
    @PreAuthorize("haseRole('ADMIN')")
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