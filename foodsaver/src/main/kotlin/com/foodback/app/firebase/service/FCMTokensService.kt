package com.foodback.app.firebase.service

import com.foodback.app.firebase.entity.FCMTokensEntity
import com.foodback.app.firebase.repository.FCMTokensRepository
import com.foodback.app.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class FCMTokensService(
    private val fcmRepository: FCMTokensRepository,
    private val userRepository: UserRepository
) {

    fun saveToken(token: String, uid: UUID): FCMTokensEntity {
        val user = userRepository.findUserById(uid)

        val existingEntity = fcmRepository.findByToken(token)
            .getOrNull()

        if (existingEntity == null) {
            // if token doesn't exist
            // чтобы не допускать дублирование

            val newEntity = FCMTokensEntity(token = token, user = user)
            return fcmRepository.save(newEntity)
        }

        return existingEntity
    }
}