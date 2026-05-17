package com.foodback.feature.base.impl.mapper

import com.foodback.feature.base.api.dto.OrderResponse
import com.foodback.feature.base.impl.entity.OrderEntity
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = [OrderItemMapper::class]
)
internal interface OrderMapper {

    fun toResponse(entity: OrderEntity): OrderResponse
}