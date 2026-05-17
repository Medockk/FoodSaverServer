package com.foodback.feature.cart.impl.service

import com.foodback.feature.cart.api.dto.AddCartItemRequest
import com.foodback.feature.cart.api.dto.CartItemResponse
import com.foodback.feature.cart.api.dto.ChangeQuantityRequest
import com.foodback.feature.cart.api.service.WriteCartService
import com.foodback.feature.cart.impl.entity.CartEntity
import com.foodback.feature.cart.impl.entity.CartItemEntity
import com.foodback.feature.cart.impl.exception.CartItemNotFoundException
import com.foodback.feature.cart.impl.mapper.CartItemMapper
import com.foodback.feature.cart.impl.repository.CartItemRepository
import com.foodback.feature.cart.impl.repository.CartRepository
import com.foodback.feature.featureProduct.api.service.ReadProductService
import com.foodback.feature.users.api.service.profile.ReadProfileService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
internal class WriteCartServiceImpl(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val cartItemMapper: CartItemMapper,

    private val productService: ReadProductService,
    private val profileService: ReadProfileService
) : WriteCartService {

    @Transactional
    override fun addCartItem(request: AddCartItemRequest, userId: UUID): CartItemResponse {
        // checking for existing product.
        // if product doesn't exist -> throw ProductNotFoundException() !
        productService.getProductById(request.productId)

        // checking for existing user
        // if user doesn't exist -> throw ...
        profileService.getProfileById(profileId = userId)

        // находим корзину пользоваткля
        val cartEntity = cartRepository.findByUserId(userId)
            ?: cartRepository // или создаём её
                .save(
                    CartEntity(
                        userId = userId
                    )
                )

        // находим таблицу связи
        val existingProducts = cartEntity.cartItems
            .find { it.productId == request.productId }

        val newQuantity = if (request.quantity < 1L) 1L
        else request.quantity
        val cartItemEntity = if (existingProducts != null) {
            // if product already in the cart
            existingProducts.quantity = newQuantity
            existingProducts
        } else {
            // if product doesn't exist in the cart
            val newCartItemEntity = CartItemEntity(
                productId = request.productId,
                cart = cartEntity,
                quantity = newQuantity
            )
            cartItemRepository.save(newCartItemEntity).let {
                // adding cartItem to cart entity
                cartEntity.cartItems.add(it)
                return@let it
            }
        }
        cartEntity.quantity = cartEntity.cartItems.size.toLong()

        return cartItemMapper.toResponse(cartItemEntity)
    }

    @Transactional
    override fun deleteCartItem(cartItemId: UUID, userId: UUID) {
        checkUser(userId, cartItemId)
        try {

            cartRepository.findByUserId(userId)?.quantity -= 1L

            cartItemRepository.deleteById(cartItemId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Transactional
    override fun changeQuantity(request: ChangeQuantityRequest, userId: UUID): CartItemResponse {
        checkUser(userId, request.cartItemId)
        val cartItemEntity = cartItemRepository
            .findById(request.cartItemId)
            .orElseThrow { CartItemNotFoundException() }

        cartItemEntity.quantity = if (request.newQuantity < 1L) {
            1L
        } else request.newQuantity

        return cartItemMapper.toResponse(cartItemEntity)
    }

    private fun checkUser(userId: UUID, cartItemId: UUID) {
        // check for exist user. otherwise - throw Exception
        profileService.getProfileById(userId)
        // check for exist cart and cart item
        cartRepository
            .findByUserIdAndCartItemsId(userId, cartItemId)
            .orElseThrow { CartItemNotFoundException() }
    }
}