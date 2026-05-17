package com.foodback.feature.category.impl.controller

import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.api.service.ReadCategoryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/category")
internal class CategoryController(
    private val readCategoryService: ReadCategoryService
) {

    @GetMapping("/all")
    fun getAllCategories(
        @PageableDefault
        pageable: Pageable
    ): ResponseEntity<Page<CategoryResponse>> {
        val categories = readCategoryService
            .getAllCategories(pageable)

        return ResponseEntity.ok(categories)
    }
}