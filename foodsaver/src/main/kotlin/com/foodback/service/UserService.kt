package com.foodback.service

import com.foodback.dto.request.user.UserRequestModel
import com.foodback.dto.response.user.UserResponseModel
import com.foodback.exception.auth.UserException
import com.foodback.mappers.toResponse
import com.foodback.repository.UserRepository
import org.springframework.stereotype.Service
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
            request.email?.let { this.email = it }
            request.name?.let { this.name = request.name }
            request.photoUrl?.let { this.photoUrl = request.photoUrl }
        }

        return userRepository.save(user).toResponse()
    }
}