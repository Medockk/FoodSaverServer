package com.foodback.feature.order.impl.mapper

import com.foodback.feature.order.api.dto.OrderItemResponse
import com.foodback.feature.order.impl.entity.OrderItemEntity
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface OrderItemMapper {

    fun toResponse(entity: OrderItemEntity): OrderItemResponse
}