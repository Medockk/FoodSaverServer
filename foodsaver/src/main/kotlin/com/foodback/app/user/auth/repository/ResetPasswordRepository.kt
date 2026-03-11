package com.foodback.app.user.auth.repository

import com.foodback.app.user.auth.entity.ResetPasswordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import java.util.UUID

/**
 * interface to access with table 'reset_password_token' in database
 */
interface ResetPasswordRepository: JpaRepository<ResetPasswordEntity, UUID> {

    /**
     * Method to delete row, if token expires or token already used
     * @return count of deleted products
     */
    @Modifying
    @Query("DELETE FROM ResetPasswordEntity t WHERE t.expiresAt <= CURRENT_TIMESTAMP() OR t.isUsed = true")
    fun deleteExpiredToken(): Int

    /**
     * Method to find [ResetPasswordEntity] by [resetToken]
     * @return An [java.util.Optional] of [ResetPasswordEntity]
     */
    fun findByResetToken(resetToken: UUID): Optional<ResetPasswordEntity>
}