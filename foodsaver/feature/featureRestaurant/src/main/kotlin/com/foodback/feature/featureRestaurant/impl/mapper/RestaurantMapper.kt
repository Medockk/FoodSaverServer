package com.foodback.feature.featureRestaurant.impl.mapper


import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
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
    @Mapping(source = "entity.companyId", target = "companyId")
    fun toResponse(entity: RestaurantEntity): RestaurantResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(
        target = "photoUris",
        expression = "java(new java.util.ArrayList<>(java.util.List.of(request.getPhotoUri())))"
    )
    @Mapping(target = "address.addressName", source = "request.address.addressName")
    @Mapping(target = "address.latitude", source = "request.address.latitude")
    @Mapping(target = "address.longitude", source = "request.address.longitude")
    @Mapping(target = "suggested", ignore = true)
    fun toEntity(request: RestaurantAddRequest): RestaurantEntity
}