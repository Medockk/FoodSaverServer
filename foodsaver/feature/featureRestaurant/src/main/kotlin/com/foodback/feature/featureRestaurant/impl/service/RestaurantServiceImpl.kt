package com.foodback.feature.featureRestaurant.impl.service

import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import com.foodback.feature.featureRestaurant.api.service.WriteRestaurantService
import com.foodback.feature.featureRestaurant.impl.repository.RestaurantRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Primary
internal class RestaurantServiceImpl(
    private val restaurantRepository: RestaurantRepository
): ReadRestaurantService, WriteRestaurantService {

    override fun getAllRestaurants(): List<RestaurantResponse> {
        TODO()
    }

    override fun getRestaurantById(id: UUID): RestaurantResponse? {
        TODO("Not yet implemented")
    }

    override fun addRestaurant(request: RestaurantAddRequest): Result<RestaurantResponse> {
        TODO("Not yet implemented")
    }
}