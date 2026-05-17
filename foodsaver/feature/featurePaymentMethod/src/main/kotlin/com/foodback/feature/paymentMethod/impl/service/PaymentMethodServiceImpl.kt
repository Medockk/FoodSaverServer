package com.foodback.feature.paymentMethod.impl.service

import com.foodback.feature.paymentMethod.api.dto.AddPaymentMethodRequest
import com.foodback.feature.paymentMethod.api.dto.PaymentMethodResponse
import com.foodback.feature.paymentMethod.api.service.PaymentMethodService
import com.foodback.feature.paymentMethod.impl.mapper.PaymentMethodMapper
import com.foodback.feature.paymentMethod.impl.repository.PaymentMethodRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
internal class PaymentMethodServiceImpl(
    private val paymentMethodRepository: PaymentMethodRepository,
    private val mapper: PaymentMethodMapper
): PaymentMethodService {

    override fun getUserPaymentMethods(userId: UUID): List<PaymentMethodResponse> {
        val method = paymentMethodRepository.findAllByUserId(userId)
        return method.map {
            mapper.toResponse(it)
        }
    }

    override fun addPaymentMethod(request: AddPaymentMethodRequest, userId: UUID): PaymentMethodResponse {
        TODO("Not yet implemented")
    }
}