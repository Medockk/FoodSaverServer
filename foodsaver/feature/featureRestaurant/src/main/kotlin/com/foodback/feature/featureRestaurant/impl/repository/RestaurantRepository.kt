package com.foodback.feature.featureRestaurant.impl.repository

import com.foodback.feature.featureRestaurant.impl.entity.RestaurantEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface RestaurantRepository: JpaRepository<RestaurantEntity, UUID> {
}