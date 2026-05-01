package com.foodback.feature.featureRestaurant.api.service

import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse

interface WriteRestaurantService: ReadRestaurantService {

    fun addRestaurant(request: RestaurantAddRequest): Result<RestaurantResponse>
}