package com.foodback.feature.base.impl.mapper

import com.foodback.feature.base.api.dto.OrderItemResponse
import com.foodback.feature.base.impl.entity.OrderItemEntity
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface OrderItemMapper {

    fun toResponse(entity: OrderItemEntity): OrderItemResponse
}