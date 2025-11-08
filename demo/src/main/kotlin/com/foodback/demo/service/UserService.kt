package com.foodback.demo.service

import com.foodback.demo.dto.request.user.UserRequestModel
import com.foodback.demo.dto.response.user.UserResponseModel
import com.foodback.demo.exception.auth.UserNotFoundException
import com.foodback.demo.mappers.toResponse
import com.foodback.demo.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import kotlin.jvm.Throws

/**
 * Service to make requests to database with same user data
 * @param userRepository Special repository to make database requests
 */
@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Throws(UserNotFoundException::class)
    fun getUser(
        uid: UUID
    ): UserResponseModel {
        val user = userRepository.findUserById(uid)

        return user.toResponse()
    }

    @Throws(UserNotFoundException::class)
    fun updateUser(
        uid: UUID,
        request: UserRequestModel
    ): UserResponseModel {
        val user = userRepository.findUserById(uid).apply {
            this.email = request.email
            this.name = request.name
            this.photoUrl = request.photoUrl
            this.updatedAt = Instant.now()
        }

        return userRepository.save(user).toResponse()
    }
}