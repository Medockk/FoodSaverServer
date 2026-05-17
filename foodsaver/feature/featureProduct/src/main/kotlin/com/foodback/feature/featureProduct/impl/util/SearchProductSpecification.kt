package com.foodback.feature.featureProduct.impl.util

import com.foodback.feature.featureProduct.impl.entity.ProductEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.util.*

internal object SearchProductSpecification {

    fun globalSearch(
        query: String?,
        discoveredIds: List<UUID>
    ): Specification<ProductEntity> {
        return Specification { root, criteriaQuery, builder ->
            val predicates = mutableListOf<Predicate>()

            // Поиск по имени продукта
            query?.takeIf { it.isNotBlank() }?.let { q ->
                predicates.add(builder.like(builder.lower(root.get("name")), "%${q.lowercase()}%"))
            }

            if (discoveredIds.isNotEmpty()) {
                // Ищем ID нужного ресторана
                predicates.add(root.get<UUID>("restaurantId").`in`(discoveredIds))

                // Ищем ID подходящей категории
                criteriaQuery.distinct(true)
                val categoryJoin = root.joinList<ProductEntity, UUID>("categoryIds")
                predicates.add(categoryJoin.`in`(discoveredIds))
            }

            if (predicates.isEmpty()) builder.conjunction()
            else builder.or(*predicates.toTypedArray())
        }
    }

    fun isNotDeleted(): Specification<ProductEntity> {
        return Specification { root, _, builder ->
            builder.equal(root.get<Boolean>("isDeleted"), false)
        }
    }
}