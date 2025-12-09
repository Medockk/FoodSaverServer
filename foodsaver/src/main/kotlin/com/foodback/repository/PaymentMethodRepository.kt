package com.foodback.repository

import com.foodback.entity.PaymentMethodEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import java.util.UUID

interface PaymentMethodRepository: JpaRepository<PaymentMethodEntity, UUID> {

    fun findAllByUser_Uid(uid: UUID): Optional<List<PaymentMethodEntity>>

    fun findByIsSelectedAndUser_uid(isSelected: Boolean, uid: UUID): Optional<PaymentMethodEntity>

    @Modifying
    @Query("UPDATE PaymentMethodEntity p SET p.isSelected = false WHERE p.user.uid = :userUid")
    fun disableCurrentPaymentMethod(userUid: UUID)
}