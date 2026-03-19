package com.foodback.app.firebase.repository

import com.foodback.app.firebase.entity.FCMTokensEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FCMTokensRepository: JpaRepository<FCMTokensEntity, UUID> {

    fun findByToken(token: String): Optional<FCMTokensEntity>
}