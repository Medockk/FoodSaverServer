package com.foodback.demo.service

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.entity.CartEntity
import com.foodback.demo.entity.CartItemEntity
import com.foodback.demo.exception.product.ProductNotFoundException
import com.foodback.demo.mappers.toResponseModel
import com.foodback.demo.repository.CartItemRepository
import com.foodback.demo.repository.CartRepository
import com.foodback.demo.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
) {

    fun addProductToCart(
        request: CartRequestModel,
        uid: String,
    ): CartItemEntity? {

        val cart = cartRepository.findByUid(uid).orElseGet {
            val entity = CartEntity(uid = uid)
            cartRepository.save(entity)
        }

        val product = productRepository.findById(request.productId).orElseThrow {
            ProductNotFoundException("Product with ID ${request.productId} not found")
        }

        val cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElseGet {
            CartItemEntity(
                cart = cart, product = product, quantity = 0
            )
        }

        cartItem.quantity += request.quantity

        if (cartItem.id == null) {
            cart.items.add(cartItem)
        }
        cart.productCount = cart.items.sumOf { it.quantity }

        return cartItemRepository.save(cartItem)
    }

    fun getUserCart(
        uid: String
    ): List<ProductResponseModel> {
        val cart = cartRepository.findByUid(uid).orElseGet {
            val entity = CartEntity(uid = uid)
            cartRepository.save(entity)
        }

        val cartItems = cartItemRepository.findAllByCart(cart).orElseGet { emptyList() }
        val products = cartItems.mapNotNull {
            it.product.toResponseModel(it.quantity)
        }

        return products
    }
}