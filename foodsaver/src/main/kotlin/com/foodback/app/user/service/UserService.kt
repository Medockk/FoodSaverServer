package com.foodback.app.user.service

import com.foodback.app.user.dto.request.UserRequestModelV1
import com.foodback.app.user.dto.response.UserResponseModelV1
import com.foodback.app.user.entity.UserEntity
import com.foodback.app.user.repository.UserRepository
import com.foodback.exception.auth.UserException
import com.foodback.service.MediaService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * Service to make requests to database with same user data
 * @param userRepository Special repository to make database requests
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val mediaService: MediaService,

    @Value($$"${app.media.path.avatars}")
    private val uploadPath: String,
) {

    /**
     * Method to get user information
     * @param uid unique user identifier of user
     * @return An [UserResponseModelV1] response with user information
     */
    @Throws(UserException::class)
    fun getUser(
        uid: UUID
    ): UserEntity {
        val user = userRepository.findUserById(uid)

        return user
    }

    /**
     * Method to update user information
     * @param uid user identifier of user to be updated
     * @param request user data to update
     * @return An [UserResponseModelV1] of new user data
     */
    @Throws(UserException::class)
    fun updateUser(
        uid: UUID,
        request: UserRequestModelV1
    ): UserEntity {
        val user = userRepository.findUserById(uid).apply {
            this.name = request.fullName
            this.email = request.email
            request.phone?.let { this.phone = request.phone }
            request.bio?.let { this.bio = request.bio }
            request.photoUrl?.let { this.photoUrl = request.photoUrl }
        }

        return user
    }

    /**
     * Return the absolute imageUrl path
     */
    @Transactional
    fun uploadAvatar(file: MultipartFile, uid: UUID): String {
        val user = userRepository.findUserById(uid)

        val relativeUrl = mediaService.uploadImage(
            file = file,
            baseUploadPath = uploadPath,
            fileUrlType = MediaService.FileUrlType.AVATARS
        )

        user.photoUrl = relativeUrl

        return mediaService.baseUrl + relativeUrl
    }
}