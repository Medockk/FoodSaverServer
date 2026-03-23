package com.foodback.dto.response.cart

import java.util.*

data class CategoriesResponseModel(
    val categoryName: String,
    val categoryId: UUID? = null
)
