package com.foodback.feature.paymentMethod.impl.repository

import com.foodback.feature.paymentMethod.impl.entity.PaymentMethodEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

internal interface PaymentMethodRepository: JpaRepository<PaymentMethodEntity, UUID> {

    fun findAllByUserId(userId: UUID): List<PaymentMethodEntity>
    fun findByUserIdAndTypeId(userId: UUID, typeId: UUID): PaymentMethodEntity?
}