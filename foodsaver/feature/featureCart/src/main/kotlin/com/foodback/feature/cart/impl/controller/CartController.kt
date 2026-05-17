package com.foodback.feature.cart.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.cart.api.dto.AddCartItemRequest
import com.foodback.feature.cart.api.dto.CartItemResponse
import com.foodback.feature.cart.api.dto.CartResponse
import com.foodback.feature.cart.api.dto.ChangeQuantityRequest
import com.foodback.feature.cart.api.dto.ProductInCartResponse
import com.foodback.feature.cart.api.service.ReadCartService
import com.foodback.feature.cart.api.service.WriteCartService
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

@RestController
@RequestMapping("/api/v1/cart")
class CartController(
    private val readCartService: ReadCartService,
    private val writeCartService: WriteCartService
) {

    @GetMapping("/my")
    fun getMyCart(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<CartResponse> {
        val cart = readCartService
            .getCartByUserId(principal.uid)
            ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(cart)
    }

    @GetMapping("/myIds")
    fun getProductIdsInCart(
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<List<ProductInCartResponse>> {
        val ids = readCartService.getProductIdsInCart(principal.uid)
        return if (ids.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(ids)
    }

    @GetMapping("/items")
    fun getMyCartItems(
        @RequestParam
        cartId: UUID,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<List<CartItemResponse>> {
        val cartItems = readCartService
            .getCartItems(cartId, principal.uid)
            .takeIf { !it.isEmpty() }
            ?: return ResponseEntity.noContent().build()

        return ResponseEntity.ok(cartItems)
    }

    @PostMapping("/add")
    fun addProductToCart(
        @RequestBody
        request: AddCartItemRequest,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<CartItemResponse> {
        val response = writeCartService.addCartItem(request, principal.uid)
        return ResponseEntity.ok(response)
    }

    @PutMapping("changeQuantity")
    fun changeQuantity(
        @RequestBody
        request: ChangeQuantityRequest,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<CartItemResponse> {
        val response = writeCartService.changeQuantity(request, principal.uid)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/delete")
    fun deleteItem(
        @RequestParam(name = "id")
        cartItemId: UUID,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ) {
        writeCartService.deleteCartItem(cartItemId, principal.uid)
    }
}