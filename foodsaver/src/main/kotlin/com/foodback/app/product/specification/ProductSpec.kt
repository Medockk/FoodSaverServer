package com.foodback.app.product.specification

import com.foodback.app.enterprises.entity.EnterprisesEntity
import com.foodback.app.product.entity.CategoryEntity
import com.foodback.app.product.entity.ProductEntity
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object ProductSpec {

    fun fromFavoriteCategories(categoryIds: Set<UUID>): Specification<ProductEntity> {
        return Specification { root, query, _ ->
            if (categoryIds.isEmpty()) return@Specification null
            query.distinct(true)
            val categoryTable = root.join<ProductEntity, CategoryEntity>("categories")
            return@Specification categoryTable?.get<UUID>("id")?.`in`(categoryIds)
        }
    }

    fun fromFavoriteEnterprises(enterpriseIds: Set<UUID>): Specification<ProductEntity> {
        return Specification { root, _, builder ->
            if (enterpriseIds.isEmpty()) return@Specification null
            val enterpriseTable = root.join<ProductEntity, EnterprisesEntity>("enterprise")
            return@Specification enterpriseTable?.get<UUID>("id")?.`in`(enterpriseIds)
        }
    }
}