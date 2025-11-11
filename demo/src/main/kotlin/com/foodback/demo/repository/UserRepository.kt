package com.foodback.demo.repository

import com.foodback.demo.entity.User.UserEntity
import com.foodback.demo.exception.auth.UserException
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Special Repository to make requests to Database with JpaRepository.
 */
interface UserRepository : JpaRepository<UserEntity, UUID> {

    /**
     * Method to find user by [username]
     * @param username Username of user to find in database
     * @return A [UserEntity] of this user or null, if user not found
     */
    fun findByUsername(username: String): UserEntity?

    /**
     * Method to find user by [id]
     * @param id unique user identifier
     * @throws UserException if user does not exist
     */
    @Throws(UserException::class)
    fun findUserById(id: UUID): UserEntity {
        return findById(id).orElseThrow {
            UserException("User with id $id not found")
        }
    }

    /**
     * Save method to find user by [email]
     * @param email user email
     */
    fun findByEmail(email: String): Optional<UserEntity>
}