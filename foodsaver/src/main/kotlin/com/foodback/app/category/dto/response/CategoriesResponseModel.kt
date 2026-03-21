package com.foodback.app.category.dto.response

import java.util.*

data class CategoryResponseModel(
    val categoryName: String,
    val categoryId: UUID? = null
)