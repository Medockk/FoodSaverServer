package com.foodback.feature.category.impl.mapper

import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.impl.entity.CategoryEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface CategoryMapper {


    fun toResponse(entity: CategoryEntity): CategoryResponse
}