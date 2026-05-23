package com.foodback.feature.category.impl.mapper

import com.foodback.feature.category.api.dto.AddCategoryRequest
import com.foodback.feature.category.api.dto.CategoryResponse
import com.foodback.feature.category.impl.entity.CategoryEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface CategoryMapper {


    fun toResponse(entity: CategoryEntity): CategoryResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    fun toEntity(request: AddCategoryRequest): CategoryEntity
}