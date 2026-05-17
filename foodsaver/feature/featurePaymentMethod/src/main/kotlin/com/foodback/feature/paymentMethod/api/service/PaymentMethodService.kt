package com.foodback.feature.paymentMethod.api.service

import com.foodback.feature.paymentMethod.api.dto.AddPaymentMethodRequest
import com.foodback.feature.paymentMethod.api.dto.PaymentMethodResponse
import java.util.UUID

interface PaymentMethodService {

    fun getUserPaymentMethods(userId: UUID): List<PaymentMethodResponse>
    fun addPaymentMethod(request: AddPaymentMethodRequest, userId: UUID): PaymentMethodResponse
}