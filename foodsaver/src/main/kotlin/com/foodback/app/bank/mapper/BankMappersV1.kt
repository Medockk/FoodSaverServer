package com.foodback.app.bank.mapper

import com.foodback.app.bank.dto.response.v1.BankResponseDtoV1
import com.foodback.app.bank.entity.BankEntity
import org.springframework.stereotype.Component

@Component
class BankMappersV1 {

    fun mapToResponse(bankEntity: BankEntity) = with(bankEntity) {
        BankResponseDtoV1(
            id = id!!,
            cardNumber = cardNumber,
            balance = balance,
            isSelected = isSelected
        )
    }
}