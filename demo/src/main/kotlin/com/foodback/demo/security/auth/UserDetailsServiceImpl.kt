package com.foodback.demo.security.auth

import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * Class to [org.springframework.security.authentication.AuthenticationManager] can authenticate user
 * @param userRepository repository to find user in database, or else throw [UserException]
 */
@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    /**
     * Authentication method
     */
    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByUsername(username)
            ?: throw UserException("User with $username not found")

        return UserDetailsImpl(userEntity)
    }
}