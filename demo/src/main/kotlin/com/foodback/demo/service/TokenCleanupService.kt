package com.foodback.demo.service

import com.foodback.demo.repository.ResetPasswordRepository
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
     * auth-launched method to delete expires or used RESET-PASSWORD tokens from database
     */
    @Transactional
    @Scheduled(fixedRate = 90_000)
    fun deleteExpiredResetPasswordToken() {
        resetPasswordRepository.deleteExpiredToken()
    }
}