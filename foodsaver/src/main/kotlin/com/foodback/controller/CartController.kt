package com.foodback.controller

import com.foodback.dto.request.cart.CartRequestModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.service.CartService
import com.foodback.utils.toUUID
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
        principal: Principal
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(
            cartService.deleteProductById(productId, principal.name.toUUID())
        )
    }

    /**
     * Method to clear user cart
     * @param principal auto-generated param, contains special UID
     */
    @DeleteMapping("all")
    fun clearCart(
        principal: Principal
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(
            cartService.clearCart(principal.name.toUUID())
        )
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
        principal: Principal
    ): ResponseEntity<ProductResponseModel> {
        val uid = principal.name.toUUID()
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
        principal: Principal
    ): ResponseEntity<ProductResponseModel> {
        val uid = principal.name.toUUID()
        val product = cartService.decreaseProduct(request, uid)

        return ResponseEntity.ok(product)
    }
}