package com.foodback.feature.featureRestaurant.api.service

import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import com.foodback.feature.featureRestaurant.api.dto.UploadRestaurantImageRequest
import java.util.UUID

interface WriteRestaurantService {

    fun addRestaurant(request: RestaurantAddRequest): RestaurantResponse
    fun uploadRestaurantImage(request: UploadRestaurantImageRequest, userRestaurantId: UUID?): String
}