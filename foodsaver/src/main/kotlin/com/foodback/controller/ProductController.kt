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
     * Method to delete product with special [productId]
     * This method can invoke only Users with authority 'DELETE_PRODUCT'
     * @param productId special product id
     */
    @DeleteMapping("{product_id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT') and @haveAuthority.canDeleteProduct(#id, authentication)")
    fun deleteProduct(
        @PathVariable("product_id")
        productId: UUID,
        @AuthenticationPrincipal
        authentication: UserDetailsImpl
    ): ResponseEntity<Void> {
        println("Authentication is: $authentication")
        productService.deleteProduct(productId)
        return ResponseEntity.ok().build()
    }

    /**
     * Method to get all products from database
     * @param size Pagination count of products
     * @param page Page in table, which need to get products
     * @return A [List] of [ProductResponseModel], contains all products or some [size] of product
     */
    @GetMapping
    fun getProducts(
        @RequestParam("size", required = false)
        size: Int = Int.MAX_VALUE,
        @RequestParam("page", required = false)
        page: Int = 0
    ): ResponseEntity<List<ProductResponseModel>> {
        val response = productService.getAllProduct(productCount = size, page = page)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to get all product containing like %[name]%
     * @param name Name of products to be found
     * @return A found [List] of [ProductResponseModel] where name equal [name]
     */
    @GetMapping("find/{name}")
    fun findProduct(
        @PathVariable("name", required = true)
        name: String
    ): ResponseEntity<List<ProductResponseModel>> {
        val result = productService.findProduct(name.lowercase())
        return ResponseEntity.ok(result)
    }

    /**
     * Method to get all [ProductResponseModel] with some [categoryId]
     * @param categoryId Identifier of category
     * @return A [List] of [ProductResponseModel] with some category
     */
    @GetMapping("/category")
    fun getProductsByCategory(
        @RequestParam("categoryId", required = true)
        categoryId: UUID
    ): ResponseEntity<List<ProductResponseModel>> {
        val result = productService.findProductsByCategory(categoryId)
        return ResponseEntity.ok(result)
    }
}