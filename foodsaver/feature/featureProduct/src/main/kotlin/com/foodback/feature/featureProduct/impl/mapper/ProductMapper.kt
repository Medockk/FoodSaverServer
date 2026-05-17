package com.foodback.feature.featureProduct.impl.mapper

import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import com.foodback.feature.featureProduct.api.dto.AddProductRequest
import com.foodback.feature.featureProduct.api.dto.ProductResponse
import com.foodback.feature.featureProduct.impl.entity.ProductEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal abstract class ProductMapper {

    @Autowired
    internal lateinit var mediaUriMapperService: MediaUriMapperService

    @Mapping(target = "isDeleted", source = "deleted") // deleted т.к. в java bytecode
    @Mapping(target = "isAvailable", source = "available")
    @Mapping(target = "imageUris", source = "imageUris", qualifiedByName = ["mapUris"])
    abstract fun toResponse(entity: ProductEntity): ProductResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "available", source = "isAvailable")
    @Mapping(target = "suggested", ignore = true)
    abstract fun toEntity(request: AddProductRequest): ProductEntity

    @Named("mapUris")
    fun relativeUriToAbsoluteUri(uris: List<String>): List<String> {
        val absoluteUris = uris.map { uri ->
            if (uri.startsWith("http")) {
                uri
            } else {
                mediaUriMapperService.toAbsoluteUri(uri)
            }
        }

        return absoluteUris
    }
}