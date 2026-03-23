package com.foodback.repository

import com.foodback.entity.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface AddressRepository: JpaRepository<AddressEntity, UUID> {

    @Modifying
    @Query("UPDATE AddressEntity a SET a.isCurrentAddress = false WHERE a.user.uid = :uid")
    fun disableAllAddresses(uid: UUID)

    fun findAllByUser_Uid(userUid: UUID): List<AddressEntity>

    fun findByUser_UidAndIsCurrentAddress(userUid: UUID, isCurrentAddress: Boolean = true): AddressEntity?

    fun deleteByIdAndUser_Uid(id: UUID, userUid: UUID)
}