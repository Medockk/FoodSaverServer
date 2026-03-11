package com.foodback.app.product.mapper

import com.foodback.app.product.dto.response.CategoriesResponseModel
import com.foodback.app.product.entity.CategoryEntity

// Map Category Entity to Response

fun CategoryEntity.toResponse() =
    CategoriesResponseModel(
        categoryName = name,
        categoryId = id
    )