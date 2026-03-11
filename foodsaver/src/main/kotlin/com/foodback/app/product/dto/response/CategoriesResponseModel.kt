package com.foodback.app.product.dto.response

import java.util.UUID

data class CategoriesResponseModel(
    val categoryName: String,
    val categoryId: UUID? = null
)