package com.foodback.feature.category.api.dto

import java.util.UUID

data class UpdateCategoryRequest(
    val id: UUID,

    val name: String? = null,
    val isDeleted: Boolean? = null
)
