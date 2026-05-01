package com.foodback.feature.featureRestaurant.impl.mapper


import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import com.foodback.feature.featureRestaurant.impl.entity.RestaurantEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface RestaurantMapper {

    @Mapping(source = "address.latitude", target = "latitude")
    @Mapping(source = "address.longitude", target = "longitude")
    @Mapping(source = "address.addressName", target = "addressName")
    fun toResponse(entity: RestaurantEntity): RestaurantResponse
}