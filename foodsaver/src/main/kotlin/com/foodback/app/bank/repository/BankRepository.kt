package com.foodback.app.bank.repository

import com.foodback.app.bank.entity.BankEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface BankRepository: JpaRepository<BankEntity, UUID> {

    fun findAllByUser_Uid(uid: UUID): List<BankEntity>

    fun findByUser_UidAndIsSelected(uid: UUID, isSelected: Boolean = true): BankEntity?

    @Modifying
    @Query("UPDATE BankEntity b SET b.isSelected = false WHERE b.user.uid = :uid")
    fun unselectedAllCards(uid: UUID)

    /**
     * Null if that new [cardNumber]
     * otherwise sam bank
     */
    fun findByCardNumber(cardNumber: String): BankEntity?

    @Modifying
    fun deleteByIdAndUser_Uid(cardId: UUID, uid: UUID)
}