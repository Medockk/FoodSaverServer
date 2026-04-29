package com.foodback.app.product.controller

import com.foodback.app.cart.service.CartService
import com.foodback.app.category.service.CategoryService
import com.foodback.app.common.dto.response.ProductResponseModel
import com.foodback.app.product.dto.request.ProductRequestModel
import com.foodback.app.product.mapper.ProductMapperV1
import com.foodback.app.product.service.PersonalizationInputData
import com.foodback.app.product.service.PersonalizationService
import com.foodback.app.product.service.ProductService
import com.foodback.app.user.service.UserService
import com.foodback.exception.auth.AuthenticationException
import com.foodback.exception.general.ErrorCode.ErrorCode
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.exception.general.ErrorCode.ServerErrorCode
import com.foodback.security.auth.UserDetailsImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


// Default constants for pageable request
private const val defaultPageSize = 15
private const val defaultPageNumber = 0

/**
 * Rest controller to process HTTP-requests
 */
@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService,
    private val categoryService: CategoryService,
    private val productMapperV1: ProductMapperV1,
    private val personalizationService: PersonalizationService
) {

    /**
     * Method to add special product to server
     * This method can invoke only Users with role ADMIN
     * If User doesn't have role ADMIN this method throw UnAuthorized exception
     * @param productRequestModel request with current product
     */
    @PostMapping
    @PreAuthorize("@haveAuthority.canAddProduct(authentication) OR hasRole('ADMIN') OR hasRole('MANAGER')")
    suspend fun addProduct(
        @RequestPart("product", required = true)
        productRequestModel: ProductRequestModel,
        @RequestPart("file")
        file: MultipartFile,
        @AuthenticationPrincipal
        principal: UserDetailsImpl
    ): ResponseEntity<ProductResponseModel> {
        val categories = categoryService.getAllCategories()
        val product = productService.addProduct(
            product = productRequestModel,
            categories = categories,
            file = file,
            userEnterprise = principal.enterprise
        )
        return ResponseEntity.ok(productMapperV1.mapToResponse(product))
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
        pageable: Pageable,
        @AuthenticationPrincipal
        userDetailsImpl: UserDetailsImpl,
        @RequestParam(required = false)
        searchRadiusKm: Double = 3.0,
        @RequestParam(required = false)
        searchType: PersonalizationInputData.SearchType = PersonalizationInputData.SearchType.NEARBY
    ): ResponseEntity<List<ProductResponseModel>> {

        val products = personalizationService.personalizeProducts(
            input = PersonalizationInputData(
                uid = userDetailsImpl.uid,
                searchRadiusKm = searchRadiusKm,
                searchType = searchType
            ),
            pageable = pageable
        )

        return if (products.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            val productResponseModels = products.map {
                productMapperV1.mapToResponse(it)
            }
            ResponseEntity.ok(productResponseModels)
        }
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
        return ResponseEntity.ok(productMapperV1.mapToResponse(product))
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
        return ResponseEntity.ok(result.map { productMapperV1.mapToResponse(it) })
    }
}