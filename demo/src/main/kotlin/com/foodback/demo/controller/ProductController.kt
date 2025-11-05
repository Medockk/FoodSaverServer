package com.foodback.demo.controller

import com.foodback.demo.dto.request.cart.ProductRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    fun deleteProduct(
        @PathVariable("product_id")
        id: UUID
    ): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}