package com.foodback.feature.featureRestaurant.api.service

import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ReadRestaurantService {

    fun getAllRestaurants(pageable: Pageable): Page<RestaurantResponse>
    fun getRestaurantById(id: UUID): RestaurantResponse?

    fun getSuggestedRestaurants(): List<RestaurantResponse>
}