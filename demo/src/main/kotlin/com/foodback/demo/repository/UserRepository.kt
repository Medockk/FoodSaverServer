package com.foodback.demo.repository

import com.foodback.demo.entity.User.UserEntity
import com.foodback.demo.exception.auth.UserNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import kotlin.jvm.Throws

/**
 * Special Repository to make requests to Database with JpaRepository.
 */
interface UserRepository: JpaRepository<UserEntity, UUID> {

    /**
     * Method to find user by email
     * @param username Username of user to find in database
     * @return A [UserEntity] of this user or null, if user not found
     */
    fun findByUsername(username: String): UserEntity?

    @Throws(UserNotFoundException::class)
    fun findUserById(id: UUID): UserEntity {
        return findById(id).orElseThrow {
            UserNotFoundException("User with id $id not found")
        }
    }
}