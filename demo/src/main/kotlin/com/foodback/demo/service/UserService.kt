package com.foodback.demo.service

import com.foodback.demo.dto.request.user.UserRequestModel
import com.foodback.demo.dto.response.user.UserResponseModel
import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.mappers.toResponse
import com.foodback.demo.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

/**
 * Service to make requests to database with same user data
 * @param userRepository Special repository to make database requests
 */
@Service
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * Method to get user information
     * @param uid unique user identifier of user
     * @return An [UserResponseModel] response with user information
     */
    @Throws(UserException::class)
    fun getUser(
        uid: UUID
    ): UserResponseModel {
        val user = userRepository.findUserById(uid)

        return user.toResponse()
    }

    /**
     * Method to update user information
     * @param uid user identifier of user to be updated
     * @param request user data to update
     * @return An [UserResponseModel] of new user data
     */
    @Throws(UserException::class)
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