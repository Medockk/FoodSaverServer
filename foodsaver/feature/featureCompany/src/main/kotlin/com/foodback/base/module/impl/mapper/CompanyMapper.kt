package com.foodback.base.module.impl.mapper

import com.foodback.base.module.api.dto.AddCompanyRequest
import com.foodback.base.module.api.dto.CompanyResponse
import com.foodback.base.module.impl.entity.CompanyEntity
import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal abstract class CompanyMapper {

    @Autowired
    internal lateinit var mediaUriMapperService: MediaUriMapperService

    @Mapping(target = "logoUri", expression = "java(mediaUriMapperService.toAbsoluteUri(entity.getLogoUri()))")
    abstract fun toResponse(entity: CompanyEntity): CompanyResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logoUri", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    abstract fun toEntity(request: AddCompanyRequest): CompanyEntity
}