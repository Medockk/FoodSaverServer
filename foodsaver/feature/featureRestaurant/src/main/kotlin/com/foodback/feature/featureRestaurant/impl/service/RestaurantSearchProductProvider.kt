package com.foodback.feature.featureRestaurant.impl.service

import com.foodback.core.coreCommon.api.search.SearchProductProvider
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import com.foodback.feature.featureRestaurant.impl.repository.RestaurantRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
internal class RestaurantSearchProductProvider(
    private val restaurantService: RestaurantRepository
): SearchProductProvider {

    override fun findProductIds(query: String): List<UUID> {
        return restaurantService
            .findAllByNameContainingIgnoreCase(query)
            .mapNotNull { it.id }
    }
}