package com.foodback.feature.category.impl.service

import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.api.service.ReadCategoryService
import com.foodback.feature.category.impl.mapper.CategoryMapper
import com.foodback.feature.category.impl.repository.CategoryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
internal class ReadCategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper
): ReadCategoryService {

    override fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAllByIsDeletedFalse()
            .map {
                categoryMapper.toResponse(it)
            }
    }

    override fun getAllCategories(pageable: Pageable): Page<CategoryResponse> {
        return categoryRepository.findAllByIsDeletedFalse(pageable)
            .map {
                categoryMapper.toResponse(it)
            }
    }

    override fun getCategoryById(id: UUID): CategoryResponse {
        val category = categoryRepository.findById(id)
            .orElseThrow()

        return categoryMapper.toResponse(category)
    }
}