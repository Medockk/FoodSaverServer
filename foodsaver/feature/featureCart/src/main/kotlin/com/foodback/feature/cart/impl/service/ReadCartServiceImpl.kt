package com.foodback.feature.cart.impl.service

import com.foodback.feature.cart.api.dto.CartItemResponse
import com.foodback.feature.cart.api.dto.CartResponse
import com.foodback.feature.cart.api.dto.ProductInCartResponse
import com.foodback.feature.cart.api.service.ReadCartService
import com.foodback.feature.cart.impl.entity.CartEntity
import com.foodback.feature.cart.impl.mapper.CartItemMapper
import com.foodback.feature.cart.impl.mapper.CartMapper
import com.foodback.feature.cart.impl.repository.CartItemRepository
import com.foodback.feature.cart.impl.repository.CartRepository
import com.foodback.feature.featureProduct.api.service.ReadProductService
import com.foodback.feature.users.api.service.profile.ReadProfileService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
internal class ReadCartServiceImpl(
    private val cartItemRepository: CartItemRepository,
    private val cartRepository: CartRepository,
    private val profileService: ReadProfileService,

    private val cartMapper: CartMapper,
    private val cartItemMapper: CartItemMapper,
    private val productService: ReadProductService
) : ReadCartService {

    override fun getCartByUserId(userId: UUID): CartResponse? {

        try {
            profileService.getProfileById(userId) // if user not exist - throw exception
        } catch (e: Exception) {
            return null // and we return null -> no content
        }

        val cartEntity = cartRepository
            .findByUserId(userId)
            // user doesn't have cart (new user)
            // creating new cart entity
            ?: cartRepository.save(
                CartEntity(
                    userId = userId
                )
            )

        val productIds = cartEntity.cartItems.mapNotNull { it.productId }
        val products = productService.getProductsByIds(productIds)
            .associateBy { it.id }

        var productsPrice = BigDecimal.ZERO
        var discountPrice = BigDecimal.ZERO

        cartEntity.cartItems.forEach { item ->
            products[item.productId]?.let { p ->
                productsPrice += p.price * BigDecimal.valueOf(item.quantity)
                discountPrice += p.discount * BigDecimal.valueOf(item.quantity)
            }
        }

        val deliveryPrice = BigDecimal.ZERO
        val finalPrice = productsPrice - discountPrice + deliveryPrice

        return CartResponse(
            id = cartEntity.id!!,
            quantity = cartEntity.cartItems.size.toLong(),
            itemsPrice = productsPrice,
            discountPrice = discountPrice,
            deliveryPrice = deliveryPrice,
            finalPrice = finalPrice
        )
    }

    override fun getCartItems(
        cartId: UUID,
        userId: UUID,
    ): List<CartItemResponse> {

        // if user doesn't exist - throw exception!
        profileService.getProfileById(userId)

        val cartItems = cartItemRepository
            .findAllByCartId(cartId)

        return cartItems.map {
            cartItemMapper.toResponse(it)
        }
    }

    override fun getProductIdsInCart(userId: UUID): List<ProductInCartResponse> {
        val products = cartRepository
            .findByUserId(userId)
            ?: return emptyList()

        return products.cartItems.map {
            ProductInCartResponse(
                productId = it.productId!!,
                cartItemId = it.id!!
            )
        }
    }
}