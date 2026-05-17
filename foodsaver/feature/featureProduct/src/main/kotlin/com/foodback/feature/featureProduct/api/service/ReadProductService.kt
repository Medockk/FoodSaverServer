package com.foodback.feature.featureProduct.api.service

import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.api.dto.SearchProductsRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ReadProductService {

    fun getProductById(productId: UUID): ProductResponse
    fun getProductsByIds(ids: List<UUID>): List<ProductResponse>

    fun getAllProducts(pageable: Pageable): Page<ProductResponse>
    fun getAllProductByRestaurantId(restaurantId: UUID, pageable: Pageable): Page<ProductResponse>

    fun getAllProductsByCategoryIds(categoryIds: List<UUID>, pageable: Pageable): Page<ProductResponse>

    fun searchProducts(request: SearchProductsRequest, pageable: Pageable): Page<ProductResponse>
    fun getSuggestedProducts(): List<ProductResponse>
}