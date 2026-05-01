package com.foodback.feature.featureRestaurant.impl.controller

import com.foodback.feature.featureRestaurant.api.service.WriteRestaurantService
import org.springframework.web.bind.annotation.RestController

@RestController("/api/v1/restaurant")
internal class RestaurantController(
    private val restaurantService: WriteRestaurantService
) {
}