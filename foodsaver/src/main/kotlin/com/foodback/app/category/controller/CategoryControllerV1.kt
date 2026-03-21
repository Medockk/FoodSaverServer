package com.foodback.app.category.controller

import com.foodback.app.category.dto.response.CategoryResponseModel
import com.foodback.app.category.mapper.CategoryMapperV1
import com.foodback.app.category.service.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/category")
class CategoryControllerV1(
    private val categoryService: CategoryService,
    private val categoryMapperV1: CategoryMapperV1
) {

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryResponseModel>> {
        val categories = categoryService.getAllCategories()
        return if (categories.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok()
            .body(categories.map {
                categoryMapperV1.mapToResponse(it)
            })
    }
}