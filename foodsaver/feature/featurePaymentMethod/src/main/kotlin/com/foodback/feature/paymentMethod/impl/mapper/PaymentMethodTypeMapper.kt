package com.foodback.feature.paymentMethod.impl.mapper

import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import com.foodback.feature.paymentMethod.api.dto.PaymentMethodTypeResponse
import com.foodback.feature.paymentMethod.impl.entity.PaymentMethodTypeEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal abstract class PaymentMethodTypeMapper {

    @Autowired
    internal lateinit var mediaUriMapperService: MediaUriMapperService

    @Mapping(target = "iconUri", source = "iconUri", qualifiedByName = ["mapUri"])
    abstract fun toResponse(entity: PaymentMethodTypeEntity): PaymentMethodTypeResponse

    @Named("mapUri")
    fun mapUri(uri: String?): String? {
        return uri?.let { uri ->
            if (uri.startsWith("http")) {
                return@let uri
            } else {
                mediaUriMapperService.toAbsoluteUri(uri)
            }
        }
    }
}