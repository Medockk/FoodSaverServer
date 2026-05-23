package com.foodback.feature.category.api.service

import com.foodback.feature.category.api.dto.AddCategoryRequest
import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.api.dto.UpdateCategoryRequest

interface WriteCategoryService {

    fun addCategory(request: AddCategoryRequest): CategoryResponse
    fun updateCategory(request: UpdateCategoryRequest): CategoryResponse
}