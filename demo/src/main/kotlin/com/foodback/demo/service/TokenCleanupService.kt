package com.foodback.demo.service

import com.foodback.demo.repository.ResetPasswordRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TokenCleanupService(
    private val resetPasswordRepository: ResetPasswordRepository
) {

    @Transactional
    @Scheduled(fixedRate = 90_000)
    fun deleteExpiredResetPasswordToken() {
        resetPasswordRepository.deleteExpiredToken()
    }
}