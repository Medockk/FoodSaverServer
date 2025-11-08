package com.foodback.demo.service

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.entity.CartEntity
import com.foodback.demo.entity.CartItemEntity
import com.foodback.demo.exception.product.ProductNotFoundException
import com.foodback.demo.mappers.toProductResponse
import com.foodback.demo.mappers.toResponseModel
import com.foodback.demo.repository.CartItemRepository
import com.foodback.demo.repository.CartRepository
import com.foodback.demo.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
) {

    @Transactional
    fun addProductToCart(
        request: CartRequestModel,
        uid: UUID,
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
        uid: UUID
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

    @Transactional
    fun clearCart(uid: UUID) {
        cartRepository.deleteAllByUid(uid)
    }

    @Transactional
    fun deleteProductById(
        productId: UUID,
        uid: UUID
    ) {
        val cart = cartRepository.findByUid(uid).orElseThrow {
            IllegalArgumentException()
        }
        val cartItemEntity = cart.items.firstOrNull {
            it.product.id == productId
        }

        if (cartItemEntity != null) {
            cart.productCount -= cartItemEntity.quantity
            cart.items.remove(cartItemEntity)
        }
    }

    @Transactional
    fun increaseProduct(
        request: CartRequestModel,
        uid: UUID
    ): ProductResponseModel {
        val cart = cartRepository.findByUid(uid).orElseThrow {
            IllegalArgumentException()
        }
        val cartItemEntity = cart.items.firstOrNull {
            it.product.id == request.productId
        }?.apply {
            quantity += request.quantity
        }

        if (cartItemEntity != null) {
            val index = cart.items.indexOf(cartItemEntity)
            cart.items[index] = cartItemEntity
            cart.productCount += request.quantity
            return cartItemEntity.toProductResponse()
        } else {
            throw IllegalArgumentException()
        }
    }

    @Transactional
    fun decreaseProduct(
        request: CartRequestModel,
        uid: UUID
    ): ProductResponseModel {

        val cart = cartRepository.findByUid(uid).orElseThrow {
            IllegalArgumentException()
        }
        val cartItemEntity = cart.items.firstOrNull {
            it.product.id == request.productId
        }?.apply {
            quantity -= request.quantity
        }

        if (cartItemEntity != null) {
            val index = cart.items.indexOf(cartItemEntity)
            cart.items[index] = cartItemEntity
            cart.productCount -= request.quantity
            return cartItemEntity.toProductResponse()
        } else {
            throw IllegalArgumentException()
        }
    }
}