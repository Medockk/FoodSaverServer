package com.foodback.feature.users.impl.service.auth

import com.foodback.feature.users.api.service.auth.ReadAuthService
import com.foodback.feature.users.impl.exception.auth.UserNotFoundException
import com.foodback.feature.users.impl.repository.AuthRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class ReadAuthServiceImpl(
    private val authRepository: AuthRepository
): ReadAuthService {

    override fun getEmailByUserId(userId: UUID): String {
        val entity = authRepository
            .findByUid(userId)
            ?: throw UserNotFoundException()

        return entity.email
    }
}