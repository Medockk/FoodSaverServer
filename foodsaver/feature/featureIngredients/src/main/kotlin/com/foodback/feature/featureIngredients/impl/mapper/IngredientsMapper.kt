package com.foodback.feature.featureIngredients.impl.mapper

import com.foodback.feature.featureIngredients.api.dto.AddIngredientRequest
import com.foodback.feature.featureIngredients.api.dto.IngredientResponse
import com.foodback.feature.featureIngredients.impl.entity.IngredientsEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface IngredientsMapper {

    fun toResponse(entity: IngredientsEntity): IngredientResponse

    @Mapping(target = "id", ignore = true)
    fun toEntity(request: AddIngredientRequest): IngredientsEntity
}