package com.foodback.app.category.mapper

import com.foodback.app.category.dto.response.CategoryResponseModel
import com.foodback.app.product.entity.CategoryEntity
import org.springframework.stereotype.Component

@Component
class CategoryMapperV1 {

    fun mapToResponse(categoryEntity: CategoryEntity): CategoryResponseModel = with(categoryEntity) {
        CategoryResponseModel(
            categoryName = name,
            categoryId = id
        )
    }
}