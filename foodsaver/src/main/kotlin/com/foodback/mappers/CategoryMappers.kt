package com.foodback.mappers

import com.foodback.dto.response.cart.CategoriesResponseModel
import com.foodback.entity.CategoryEntity

// Map Category Entity to Response

fun CategoryEntity.toResponse() =
    CategoriesResponseModel(
        categoryName = name,
        categoryId = id
    )