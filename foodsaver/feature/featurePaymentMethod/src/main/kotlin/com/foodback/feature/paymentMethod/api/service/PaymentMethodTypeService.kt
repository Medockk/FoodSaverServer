package com.foodback.feature.paymentMethod.api.service

import com.foodback.feature.paymentMethod.api.dto.PaymentMethodTypeResponse

interface PaymentMethodTypeService {

    fun getTypes(): List<PaymentMethodTypeResponse>
}