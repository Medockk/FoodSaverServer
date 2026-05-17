package com.foodback.feature.cart.api.service

import com.foodback.feature.cart.api.dto.CartItemResponse
import com.foodback.feature.cart.api.dto.CartResponse
import com.foodback.feature.cart.api.dto.ProductInCartResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ReadCartService {

    fun getCartByUserId(userId: UUID): CartResponse?
    fun getCartItems(cartId: UUID, userId: UUID): List<CartItemResponse>

    fun getProductIdsInCart(userId: UUID): List<ProductInCartResponse>
}