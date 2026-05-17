package com.foodback.feature.cart.impl.mapper

import com.foodback.feature.cart.api.dto.CartItemResponse
import com.foodback.feature.cart.impl.entity.CartItemEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface CartItemMapper {

    @Mapping(target = "attributes", ignore = true)
    fun toResponse(entity: CartItemEntity): CartItemResponse
}