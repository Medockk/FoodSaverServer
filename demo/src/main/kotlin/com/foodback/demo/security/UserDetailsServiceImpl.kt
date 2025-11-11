package com.foodback.demo.security

import com.foodback.demo.exception.auth.UserException
import com.foodback.demo.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByUsername(username)
            ?: throw UserException("User with $username not found")

        return UserDetailsImpl(userEntity)
    }
}