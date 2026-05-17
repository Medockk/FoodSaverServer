package com.foodback.feature.paymentMethod.impl.mapper

import com.foodback.feature.paymentMethod.api.dto.PaymentMethodResponse
import com.foodback.feature.paymentMethod.impl.entity.PaymentMethodEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.Named

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = [PaymentMethodTypeMapper::class]
)
internal interface PaymentMethodMapper {

    @Mapping(target = "lastFourSymbols", source = "cartNumber", qualifiedByName = ["mapSymbols"])
//    @Mapping(target = "selected", source = "selected")
    fun toResponse(entity: PaymentMethodEntity): PaymentMethodResponse

    @Named("mapSymbols")
    fun mapLastFourSymbols(symbols: String?): String? {
        return symbols?.takeLast(4)
    }
}