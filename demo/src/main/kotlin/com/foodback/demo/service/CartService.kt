package com.foodback.demo.service

import com.foodback.demo.dto.request.cart.CartRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.entity.CartEntity
import com.foodback.demo.entity.CartItemEntity
import com.foodback.demo.exception.cart.CartException
import com.foodback.demo.exception.general.ErrorCode.RequestError
import com.foodback.demo.exception.product.ProductNotFoundException
import com.foodback.demo.mappers.toProductResponse
import com.foodback.demo.mappers.toResponseModel
import com.foodback.demo.repository.CartItemRepository
import com.foodback.demo.repository.CartRepository
import com.foodback.demo.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Special service to do some logic with user cart
 * @param cartRepository special object of [org.springframework.data.jpa.repository.JpaRepository] - repository to
 * access to cart database
 * @param cartItemRepository special object of [org.springframework.data.jpa.repository.JpaRepository] - repository to
 *  * access to cart_item database (special table contains information about product_id and cart_id)
 *  @param productRepository special object of [org.springframework.data.jpa.repository.JpaRepository] - repository to
 *  * access to product database
 */
@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
) {

    /**
     * Method to add special product to cart
     * @param request request contains product id
     * @param uid user identifier
     */
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

    /**
     * Method to get all products contains in user cart
     * @param uid identifier of current user
     * @return A [List] of [ProductResponseModel]
     */
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

    /**
     * Method to clear user cart from all products
     * @param uid identifier of current user
     */
    @Transactional
    fun clearCart(uid: UUID) {
        cartRepository.deleteAllByUid(uid)
    }

    /**
     * Method to delete product from cart
     * @param productId id of product to delete
     * @param uid user identifier
     */
    @Transactional
    fun deleteProductById(
        productId: UUID,
        uid: UUID
    ) {
        val cart = cartRepository.findByUid(uid).orElseThrow {
            CartException(RequestError.CartRequest.CART_NOT_FOUND, "Cart not found")
        }
        val cartItemEntity = cart.items.firstOrNull {
            it.product.id == productId
        }

        if (cartItemEntity != null) {
            cart.productCount -= cartItemEntity.quantity
            cart.items.remove(cartItemEntity)
        } else {
            throw ProductNotFoundException()
        }
    }

    /**
     * Method to increase product count in user cart
     * @param request Special request, contains product id, which needs to be increase
     * @param uid identifier of current user
     * @return A [ProductResponseModel] new information about product which increased
     */
    @Transactional
    fun increaseProduct(
        request: CartRequestModel,
        uid: UUID
    ): ProductResponseModel {
        val cart = cartRepository.findByUid(uid).orElseThrow {
            CartException(RequestError.CartRequest.CART_NOT_FOUND, "Cart not found")
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
            throw ProductNotFoundException()
        }
    }

    /**
     * Method to decrease product count in user cart
     * @param request Special request, contains product id, which needs to be decrease
     * @param uid identifier of current user
     * @return A [ProductResponseModel] new information about product which decreased
     */
    @Transactional
    fun decreaseProduct(
        request: CartRequestModel,
        uid: UUID
    ): ProductResponseModel {

        val cart = cartRepository.findByUid(uid).orElseThrow {
            CartException(RequestError.CartRequest.CART_NOT_FOUND, "Cart not found")
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
            throw ProductNotFoundException()
        }
    }
}