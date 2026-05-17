package com.foodback.feature.featureProduct.impl.controller

import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.api.dto.SearchProductsRequest
import com.foodback.feature.featureProduct.api.service.ReadProductService
import com.foodback.feature.featureProduct.impl.service.SearchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/products/search")
internal class SearchController(
    private val searchService: SearchService,
    private val readProductService: ReadProductService
) {

    @GetMapping("/query")
    fun search(
        @RequestParam(name = "q")
        query: String,
        @PageableDefault
        @SortDefault.SortDefaults(
            SortDefault(sort = ["expiresAt"], direction = Sort.Direction.DESC),
        )
        pageable: Pageable
    ): ResponseEntity<Page<ProductResponse>> {
        val result = searchService.search(query, pageable)
        return if (result.isEmpty) ResponseEntity.noContent().build()
        else ResponseEntity.ok(result)
    }

    @GetMapping("/category")
    fun searchByCategoryId(
        @RequestParam
        categoryId: UUID,
        @PageableDefault
        pageable: Pageable
    ): ResponseEntity<Page<ProductResponse>> {
        val result = readProductService.getAllProductsByCategoryIds(
            listOf(categoryId),
            pageable
        )
        return if (result.isEmpty) ResponseEntity.noContent().build()
        else ResponseEntity.ok(result)
    }
}