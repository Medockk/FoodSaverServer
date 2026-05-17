package com.foodback.feature.category.api.service

import com.foodback.feature.category.api.dto.CategoryResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ReadCategoryService {

    fun getAllCategories(): List<CategoryResponse>
    fun getAllCategories(pageable: Pageable): Page<CategoryResponse>
}