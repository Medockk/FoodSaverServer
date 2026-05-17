package com.foodback.feature.paymentMethod.impl.service

import com.foodback.feature.paymentMethod.api.dto.PaymentMethodTypeResponse
import com.foodback.feature.paymentMethod.api.service.PaymentMethodTypeService
import com.foodback.feature.paymentMethod.impl.mapper.PaymentMethodTypeMapper
import com.foodback.feature.paymentMethod.impl.repository.PaymentMethodTypeRepository
import org.springframework.stereotype.Service

@Service
internal class PaymentMethodTypeServiceImpl(
    private val paymentMethodTypeRepository: PaymentMethodTypeRepository,
    private val mapper: PaymentMethodTypeMapper
): PaymentMethodTypeService {

    override fun getTypes(): List<PaymentMethodTypeResponse> {
        val types = paymentMethodTypeRepository
            .findAll()

        return types.map {
            mapper.toResponse(it)
        }
    }
}