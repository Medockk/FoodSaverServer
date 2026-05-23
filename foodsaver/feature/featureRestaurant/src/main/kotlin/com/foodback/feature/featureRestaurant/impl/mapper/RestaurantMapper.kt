package com.foodback.feature.featureRestaurant.impl.mapper


import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import com.foodback.feature.featureRestaurant.api.dto.RestaurantAddRequest
import com.foodback.feature.featureRestaurant.api.dto.RestaurantResponse
import com.foodback.feature.featureRestaurant.impl.entity.RestaurantEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal abstract class RestaurantMapper {

    @Autowired
    lateinit var mediaUriMapperService: MediaUriMapperService

    @Mapping(source = "address.latitude", target = "latitude")
    @Mapping(source = "address.longitude", target = "longitude")
    @Mapping(source = "address.addressName", target = "addressName")
    @Mapping(source = "entity.companyId", target = "companyId")
    @Mapping(target = "photoUris", source = "photoUris", qualifiedByName = ["mapUris"])
    abstract fun toResponse(entity: RestaurantEntity): RestaurantResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address.addressName", source = "request.address.addressName")
    @Mapping(target = "address.latitude", source = "request.address.latitude")
    @Mapping(target = "address.longitude", source = "request.address.longitude")
    @Mapping(target = "suggested", ignore = true)
    abstract fun toEntity(request: RestaurantAddRequest): RestaurantEntity

    @Named("mapUris")
    fun mapUris(uris: List<String>): List<String> {
        return uris.map { mediaUriMapperService.toAbsoluteUri(it) }
    }
}