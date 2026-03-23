package com.foodback.service

import com.foodback.dto.request.user.UserRequestModel
import com.foodback.dto.response.user.UserResponseModel
import com.foodback.exception.auth.UserException
import com.foodback.mappers.UserMappers
import com.foodback.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Service to make requests to database with same user data
 * @param userRepository Special repository to make database requests
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMappers: UserMappers,

    @Value($$"${server.address}")
    private val serverAddress: String,
    @Value($$"${server.port}")
    private val serverPort: String,
    @Value($$"${app.media.path.avatars}")
    private val uploadPath: String,
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

        return with(userMappers) {
            user.toResponse()
        }
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
            this.name = request.fullName
            this.email = request.email
            request.phone?.let { this.phone = request.phone }
            request.bio?.let { this.bio = request.bio }
            request.photoUrl?.let { this.photoUrl = request.photoUrl }
        }
        println(user)

        return with(userMappers) { userRepository.save(user).toResponse() }
    }

    @Transactional
    fun uploadAvatar(file: MultipartFile, uid: UUID): String {
        if (file.isEmpty) throw UserException("File is empty!")
        val user = userRepository.findUserById(uid)

        val uploadDir = File(uploadPath)
        if (!uploadDir.exists()) uploadDir.mkdirs()

        val fileName = "${UUID.randomUUID()}_${file.originalFilename}.jpg"
        val path = Paths.get(uploadPath, fileName)

        Files.write(path, file.bytes)
        val fileUrl = "media/avatars/$fileName"
        user.photoUrl = fileUrl

        val url = "http://$serverAddress:$serverPort/$fileUrl"
        return url
    }
}