package com.foodback.app.product.dto.response

import java.util.*

data class CategoriesResponseModel(
    val categoryName: String,
    val categoryId: UUID? = null
)