package com.foodback.feature.featureProduct.impl.service

import com.foodback.core.coreCommon.api.search.SearchProductProvider
import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.api.dto.SearchProductsRequest
import com.foodback.feature.featureProduct.api.service.ReadProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
internal class SearchService(
    private val productService: ReadProductService,
    private val searchProductProviders: List<SearchProductProvider>
) {

    fun search(
        query: String,
        pageable: Pageable
    ): Page<ProductResponse> {
        val discoveredIds = searchProductProviders
            .flatMap { it.findProductIds(query) }
        val request = SearchProductsRequest(query, discoveredIds)
        val searchResult = productService
            .searchProducts(request, pageable)
        return searchResult
    }
}