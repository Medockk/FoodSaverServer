package com.foodback.feature.category.impl.service

import com.foodback.core.coreCommon.api.search.SearchProductProvider
import com.foodback.feature.category.impl.repository.CategoryRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
internal class CategorySearchProductProvider(
    private val categoryRepository: CategoryRepository
): SearchProductProvider {

    override fun findProductIds(query: String): List<UUID> {
        val categories = categoryRepository.findAllByNameContainingIgnoreCaseAndIsDeletedFalse(query)
            .mapNotNull { it.id }
        println("Searched categories $categories")
        return categories
    }
}