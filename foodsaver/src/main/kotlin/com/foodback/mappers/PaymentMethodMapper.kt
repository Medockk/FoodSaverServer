package com.foodback.mappers

import com.foodback.dto.response.payment.PaymentResponseModel
import com.foodback.entity.PaymentMethodEntity
import org.springframework.stereotype.Component

@Component
class PaymentMethodMapper {

    fun toResponseModel(paymentMethodEntity: PaymentMethodEntity) =
        PaymentResponseModel(
            id = paymentMethodEntity.id!!.toString(),
            bank = paymentMethodEntity.bank,
            cardNumber = paymentMethodEntity.cardNumber,
            isSelected = paymentMethodEntity.isSelected,
        )

    fun toResponseModel(paymentMethodEntity: List<PaymentMethodEntity>) = paymentMethodEntity.map {
        toResponseModel(it)
    }
}