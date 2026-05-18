package com.foodback.feature.address.impl.mapper

import com.foodback.feature.address.api.dto.AddAddressRequest
import com.foodback.feature.address.api.dto.AddressResponse
import com.foodback.feature.address.impl.entity.AddressEntity
import org.locationtech.jts.geom.GeometryFactory
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.MappingTarget
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal abstract class AddressMapper {

    @Autowired
    protected lateinit var geometryFactory: GeometryFactory

    @Mapping(target = "fullAddress", source = "entity", qualifiedByName = ["toFullAddress"])
    abstract fun toResponse(entity: AddressEntity): AddressResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "coords", ignore = true)
    abstract fun toEntity(request: AddAddressRequest): AddressEntity

    @AfterMapping
    protected fun fillCoords(request: AddAddressRequest, @MappingTarget entity: AddressEntity) {
        entity.updateCoords(request.latitude, request.longitude, geometryFactory)
    }

    @Named("toFullAddress")
    fun toFullAddress(entity: AddressEntity): String {
        return buildString {
            append("${entity.city}, ${entity.street}, д. ${entity.house}")
            entity.apartment?.let { append(", кв. $it") }
            entity.entrance?.let { append(", под. $it") }
            entity.floor?.let { append(", эт. $it") }
        }
    }
}