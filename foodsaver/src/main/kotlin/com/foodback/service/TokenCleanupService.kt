package com.foodback.service

import com.foodback.repository.ResetPasswordRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service to do some logic to auto-delete tokens
 */
@Service
class TokenCleanupService(
    private val resetPasswordRepository: ResetPasswordRepository
) {

    /**
     * Auto-launched method to delete expires or used RESET-PASSWORD tokens from database.
     * This method launched every 90 seconds.
     */
    @Transactional
    @Scheduled(fixedRate = 90_000)
    fun deleteExpiredResetPasswordToken() {
        resetPasswordRepository.deleteExpiredToken()
    }
}