package com.foodback.feature.category.impl.service

import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.api.service.ReadCategoryService
import com.foodback.feature.category.impl.mapper.CategoryMapper
import com.foodback.feature.category.impl.repository.CategoryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
internal class ReadCategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper
): ReadCategoryService {

    override fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAll()
            .map {
                categoryMapper.toResponse(it)
            }
    }

    override fun getAllCategories(pageable: Pageable): Page<CategoryResponse> {
        return categoryRepository.findAll(pageable)
            .map {
                categoryMapper.toResponse(it)
            }
    }
}