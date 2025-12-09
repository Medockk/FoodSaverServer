package com.foodback.controller

import com.foodback.dto.request.cart.ProductRequestModel
import com.foodback.dto.response.cart.CategoriesResponseModel
import com.foodback.dto.response.cart.ProductResponseModel
import com.foodback.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*


// Default constants for pageable request
private const val defaultPageSize = 15
private const val defaultPageNumber = 0

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
     * If User doesn't have role ADMIN this method throw UnAuthorized exception
     * @param productRequestModel request with current product
     */
    @PostMapping
    @PreAuthorize("@haveAuthority.canAddProduct(authentication) OR hasRole('ADMIN')")
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
    @PreAuthorize("hasAuthority('DELETE_PRODUCT') and @haveAuthority.canDeleteProduct(#productId, authentication)")
    fun deleteProduct(
        @PathVariable("product_id")
        productId: UUID
    ): ResponseEntity<Void> {
        productService.deleteProduct(productId)
        return ResponseEntity.ok().build()
    }

    /**
     * Method to get all products from database
     * @param pageable This parameter set LIMIT and OFFSET in SQL-query
     * @return A [List] of [ProductResponseModel]
     */
    @GetMapping
    fun getProducts(
        @PageableDefault(size = defaultPageSize, page = defaultPageNumber)
        @SortDefault.SortDefaults(
            SortDefault(sort = ["expiresAt"], direction = Sort.Direction.ASC),
            SortDefault(sort = ["rating"], direction = Sort.Direction.DESC)
        )
        pageable: Pageable
    ): ResponseEntity<List<ProductResponseModel>> {
        val response = productService.getAllProduct(pageable)
        return ResponseEntity.ok(response)
    }

    /**
     * Method to find product by [productId]
     * @param productId Identifier of current product
     * @throws com.foodback.exception.product.ProductNotFoundException If failed to found product with id [productId]
     * @return A [ResponseEntity] of [ProductResponseModel]
     */
    @GetMapping("/id/{productId}")
    fun getProductById(
        @PathVariable(value = "productId", required = true)
        productId: UUID
    ): ResponseEntity<ProductResponseModel> {
        val product = productService.getProductById(productId)
        return ResponseEntity.ok(product)
    }

    /**
     * Method to get all product containing like %[name]%
     * @param name Name of products to be found
     * @return A found [List] of [ProductResponseModel] where name equal [name]
     */
    @GetMapping("search")
    fun searchProduct(
        @RequestParam("name", required = false, defaultValue = "")
        name: String?,
        @RequestParam("categoryIds", required = false)
        categoryIds: List<UUID>?,
        @PageableDefault(size = defaultPageSize, page = defaultPageNumber)
        @SortDefault.SortDefaults(
            SortDefault(sort = ["expiresAt"], direction = Sort.Direction.ASC),
            SortDefault(sort = ["rating"], direction = Sort.Direction.DESC)
        )
        pageable: Pageable
    ): ResponseEntity<List<ProductResponseModel>> {
        val result = productService.searchProducts(
            productName = name ?: "",
            categoryIds = categoryIds ?: emptyList(),
            pageable = pageable
        )
        return ResponseEntity.ok(result)
    }

    /**
     * Method to get all categories
     * @return A [List] of [CategoriesResponseModel]
     */
    @GetMapping("/allCategories")
    fun getAllCategories(): ResponseEntity<List<CategoriesResponseModel>> {
        return ResponseEntity.ok(
            productService.getAllCategories()
        )
    }

    /**
     * Method to get all/some products with special [organizationId]. If [categoryId] isn't null,
     * get all/some products belongs [organizationId] with special [categoryId].
     * @param organizationId The identifier of organization issuing products
     * @param categoryId The identifier of product category
     * @param pageable This parameter set LIMIT and OFFSET in SQL-query
     * @return [ResponseEntity] of [List] a [ProductResponseModel]
     */
    @GetMapping("/organization/{organization}")
    fun getProductsByOrganizationWithCategory(
        @PathVariable("organization", required = true)
        organizationId: UUID,
        @RequestParam("categoryId", required = false)
        categoryId: UUID?,
        @PageableDefault(size = defaultPageSize, page = defaultPageNumber)
        @SortDefault.SortDefaults(
            SortDefault(sort = ["expiresAt"], direction = Sort.Direction.ASC),
            SortDefault(sort = ["rating"], direction = Sort.Direction.DESC)
        )
        pageable: Pageable
    ): ResponseEntity<List<ProductResponseModel>> {

        val products = productService.getProductsByOrganization(
            organizationId = organizationId,
            categoryId = categoryId,
            pageable = pageable
        )

        return ResponseEntity.ok(products)
    }
}