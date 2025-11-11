package com.foodback.demo.repository

import com.foodback.demo.entity.ResetPasswordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ResetPasswordRepository: JpaRepository<ResetPasswordEntity, UUID> {

    @Modifying
    @Query("DELETE FROM ResetPasswordEntity t WHERE t.expiresAt <= CURRENT_TIMESTAMP() OR t.isUsed = true")
    fun deleteExpiredToken(): Int

    fun findByResetToken(resetToken: UUID): Optional<ResetPasswordEntity>
}