package com.foodback.feature.featureRestaurant.api.service

import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import java.util.UUID

interface ReadRestaurantService {

    fun getAllRestaurants(): List<RestaurantResponse>
    fun getRestaurantById(id: UUID): RestaurantResponse?
}