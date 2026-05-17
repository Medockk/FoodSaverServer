package com.foodback.feature.paymentMethod.impl.repository

import com.foodback.feature.paymentMethod.impl.entity.PaymentMethodTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface PaymentMethodTypeRepository: JpaRepository<PaymentMethodTypeEntity, UUID> {
}