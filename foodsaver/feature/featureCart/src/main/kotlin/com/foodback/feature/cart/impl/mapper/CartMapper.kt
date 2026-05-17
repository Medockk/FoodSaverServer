package com.foodback.feature.cart.impl.mapper

import com.foodback.feature.cart.api.dto.CartResponse
import com.foodback.feature.cart.impl.entity.CartEntity
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface CartMapper {


    fun toResponse(entity: CartEntity): CartResponse
}