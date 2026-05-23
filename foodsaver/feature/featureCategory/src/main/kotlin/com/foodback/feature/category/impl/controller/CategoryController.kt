package com.foodback.feature.category.impl.controller

import com.foodback.feature.category.api.dto.AddCategoryRequest
import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.api.dto.UpdateCategoryRequest
import com.foodback.feature.category.api.service.ReadCategoryService
import com.foodback.feature.category.api.service.WriteCategoryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/category")
internal class CategoryController(
    private val readCategoryService: ReadCategoryService,
    private val writeCategoryService: WriteCategoryService
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

    @GetMapping("/id")
    fun getCategoryById(
        @RequestParam id: UUID
    ): ResponseEntity<CategoryResponse> {
        val response = readCategoryService.getCategoryById(id)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADD_CATEGORY')")
    fun addCategory(
        @RequestBody
        request: AddCategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val response = writeCategoryService.addCategory(request)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('EDIT_CATEGORY')")
    fun updateCategory(
        @RequestBody
        request: UpdateCategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val response = writeCategoryService.updateCategory(request)
        return ResponseEntity.ok(response)
    }
}