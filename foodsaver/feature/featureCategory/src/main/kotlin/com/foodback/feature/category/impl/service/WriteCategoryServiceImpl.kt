package com.foodback.feature.category.impl.service

import com.foodback.feature.category.api.dto.AddCategoryRequest
import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.api.dto.UpdateCategoryRequest
import com.foodback.feature.category.api.service.WriteCategoryService
import com.foodback.feature.category.impl.mapper.CategoryMapper
import com.foodback.feature.category.impl.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class WriteCategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper
): WriteCategoryService {

    override fun addCategory(request: AddCategoryRequest): CategoryResponse {
        // check for existing categories by name
        val isCategoryExist = categoryRepository.findByNameContainingIgnoreCase(request.name) != null
        if (isCategoryExist) throw Exception()

        val entity = categoryMapper.toEntity(request)
        val savedCategory = categoryRepository.save(entity)
        return categoryMapper.toResponse(savedCategory)
    }

    @Transactional
    override fun updateCategory(request: UpdateCategoryRequest): CategoryResponse {
        val category = categoryRepository.findById(request.id)
            .orElseThrow()

        request.name?.let { category.name = it }
        request.isDeleted?.let { category.isDeleted = it }

        return categoryMapper.toResponse(category)
    }
}