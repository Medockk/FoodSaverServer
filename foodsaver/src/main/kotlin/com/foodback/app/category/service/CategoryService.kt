package com.foodback.app.category.service

import com.foodback.app.product.entity.CategoryEntity
import com.foodback.app.product.repository.ProductCategoriesRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: ProductCategoriesRepository
) {

    fun getAllCategories(): List<CategoryEntity> {
        return categoryRepository.findAll()
    }
}