package com.foodback.demo.controller

import com.foodback.demo.dto.request.cart.ProductRequestModel
import com.foodback.demo.dto.response.cart.ProductResponseModel
import com.foodback.demo.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun addProduct(
        @RequestBody(required = true)
        productRequestModel: ProductRequestModel
    ): ResponseEntity<ProductResponseModel> {
        val product = productService.addProduct(productRequestModel)
        return ResponseEntity.ok(product)
    }

    @DeleteMapping("{product_id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    fun deleteProduct(
        @PathVariable("product_id")
        id: UUID
    ): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}