package com.foodback.controller

import com.foodback.dto.request.cart.ProductRequestModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.security.auth.UserDetailsImpl
import com.foodback.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Rest controller to process HTTP-requests
 */
@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    /**
     * Method to add special product to server
     * This method can invoke only Users with role ADMIN
     * If User doens't have role ADMIN this method throw UnAuthorized exception
     * @param productRequestModel request with current product
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun addProduct(
        @RequestBody(required = true)
        productRequestModel: ProductRequestModel
    ): ResponseEntity<ProductResponseModel> {
        val product = productService.addProduct(productRequestModel)
        return ResponseEntity.ok(product)
    }

    /**
     * Method to delete product with special [id]
     * This method can invoke only Users with authority 'DELETE_PRODUCT'
     * @param id special product id
     */
    @DeleteMapping("{product_id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT') and @haveAuthority.canDeleteProduct(#id, authentication)")
    fun deleteProduct(
        @PathVariable("product_id")
        id: UUID,
        @AuthenticationPrincipal
        authentication: UserDetailsImpl
    ): ResponseEntity<Void> {
        println("Authentication is: $authentication")
        return ResponseEntity.ok().build()
    }

    /**
     * Method to get all products from database
     * @param count Pagination count of products
     * @return A [List] of [ProductResponseModel], contains all products or some [count] of product
     */
    @GetMapping
    fun getProducts(
        @RequestParam("count", required = false)
        count: Int = Int.MAX_VALUE
    ): ResponseEntity<List<ProductResponseModel>> {
        return ResponseEntity.ok(productService.getAllProduct(productCount = count))
    }
}