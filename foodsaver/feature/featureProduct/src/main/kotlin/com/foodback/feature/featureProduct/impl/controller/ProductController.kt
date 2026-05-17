package com.foodback.feature.featureProduct.impl.controller

import com.foodback.core.coreSecurity.api.dto.SecurityPrincipal
import com.foodback.feature.featureProduct.api.dto.AddProductRequest
import com.foodback.feature.featureProduct.api.dto.EditProductRequest
import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.api.dto.UploadProductImageRequest
import com.foodback.feature.featureProduct.api.service.ReadProductService
import com.foodback.feature.featureProduct.api.service.WriteProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/v1/products")
internal class ProductController(
    private val readProductService: ReadProductService,
    private val writeProductService: WriteProductService
) {

    @GetMapping("/id")
    fun getProductById(
        @RequestParam
        productId: UUID
    ): ResponseEntity<ProductResponse> {
        val product = readProductService
            .getProductById(productId)

        return ResponseEntity
            .ok(product)
    }

    @GetMapping("/ids")
    fun getProductsByIds(
        @RequestParam
        ids: List<UUID>
    ): ResponseEntity<List<ProductResponse>> {
        val products = readProductService.getProductsByIds(ids)
        return ResponseEntity.ok(products)
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADD_PRODUCT')")
    fun addProduct(
        @RequestBody
        request: AddProductRequest,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<ProductResponse> {
        val userRestaurantId = principal.restaurantId
        val result = writeProductService
            .addProduct(request, userRestaurantId)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/all")
    fun getAllProducts(
        @PageableDefault
        @SortDefault.SortDefaults(
            SortDefault(sort = ["expiresAt"], direction = Sort.Direction.ASC),
        )
        pageable: Pageable
    ): ResponseEntity<Page<ProductResponse>> {
        val products = readProductService
            .getAllProducts(pageable)

        return ResponseEntity.ok(products)
    }

    @GetMapping("/restaurant")
    fun getAllProductsByRestaurantId(
        @PageableDefault
        @SortDefault.SortDefaults(
            SortDefault(sort = ["expiresAt"], direction = Sort.Direction.ASC),
        )
        pageable: Pageable,
        @RequestParam("restaurantId")
        restaurantId: UUID
    ): ResponseEntity<Page<ProductResponse>> {
        val products = readProductService
            .getAllProductByRestaurantId(restaurantId, pageable)

        return ResponseEntity.ok(products)
    }

    @GetMapping("/categories")
    fun getAllProductsByCategoryIds(
        @PageableDefault
        @SortDefault.SortDefaults(
            SortDefault(sort = ["expiresAt"], direction = Sort.Direction.ASC),
        )
        pageable: Pageable,
        @RequestParam("categoryIds")
        categoryIds: List<UUID>
    ): ResponseEntity<Page<ProductResponse>> {
        val products = readProductService
            .getAllProductsByCategoryIds(categoryIds, pageable)
        return ResponseEntity.ok(products)
    }

    @PostMapping("/uploadImage")
    @PreAuthorize("hasAuthority('ADD_PRODUCT')")
    fun uploadImage(
        @RequestPart
        image: MultipartFile,
        @RequestParam(required = false)
        productId: UUID?,
        @RequestParam("ext", required = false)
        imageExtension: String?,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<String> {
        val restaurantId = principal.restaurantId
            ?: return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build()

        val imageUri = writeProductService.uploadImage(
            request = UploadProductImageRequest(
                image = image.bytes,
                productId = productId,
                restaurantId = restaurantId,
                imageExtension = imageExtension
            ),
            userRestaurantId = principal.restaurantId,
        )

        return ResponseEntity.ok(imageUri)
    }

    @PatchMapping("update")
    @PreAuthorize("hasAuthority('EDIT_PRODUCT') OR hasRole('ADMIN')")
    fun updateProduct(
        @RequestBody
        request: EditProductRequest,
        @AuthenticationPrincipal
        principal: SecurityPrincipal
    ): ResponseEntity<ProductResponse> {
        val product = writeProductService
            .editProduct(request, principal.restaurantId)

        return ResponseEntity.ok(product)
    }

    @GetMapping("/suggested")
    fun getSuggestedProducts(): ResponseEntity<List<ProductResponse>> {
        val products = readProductService.getSuggestedProducts()
        return if (products.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(products)
    }
}