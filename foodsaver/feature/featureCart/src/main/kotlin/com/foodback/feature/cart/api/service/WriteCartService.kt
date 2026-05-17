package com.foodback.feature.cart.api.service

import com.foodback.feature.cart.api.dto.AddCartItemRequest
import com.foodback.feature.cart.api.dto.CartItemResponse
import com.foodback.feature.cart.api.dto.ChangeQuantityRequest
import java.util.UUID

interface WriteCartService {

    fun addCartItem(request: AddCartItemRequest, userId: UUID): CartItemResponse
    fun deleteCartItem(cartItemId: UUID, userId: UUID)
    fun changeQuantity(request: ChangeQuantityRequest, userId: UUID): CartItemResponse
}