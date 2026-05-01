package com.foodback.feature.users.impl.service

import com.foodback.feature.users.api.dto.AuthResponse
import com.foodback.feature.users.api.dto.GoogleRequest
import com.foodback.feature.users.api.service.AuthGoogleService
import com.foodback.feature.users.impl.entity.UserEntity
import com.foodback.feature.users.impl.exception.InvalidGoogleTokenException
import com.foodback.feature.users.impl.repository.UserRepository
import com.foodback.feature.users.impl.util.AuthResponseUtil
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthGoogleServiceImpl(
    private val userRepository: UserRepository,
    private val authResponseUtil: AuthResponseUtil,
    private val verifier: GoogleIdTokenVerifier
): AuthGoogleService {

    @Transactional
    override fun authorizeWithGoogle(request: GoogleRequest): AuthResponse {
        val idToken = verifier.verify(request.googleId)
            ?: throw InvalidGoogleTokenException()

        val payload = idToken.payload
        val googleId = payload.subject

        // search via Google token
        val user = userRepository.findByGoogleId(googleId)
        if (user != null) {
            return authResponseUtil.buildAuthResponse(user)
        }

        // is this user already registered?
        // search by email
        val userByEmail = userRepository.findByEmail(payload.email)
        if (userByEmail != null) {
            userByEmail.apply {
                // update запись, т.к. чел входит в аккаунт с помощью гугла
                this.googleId = googleId
            }
            return authResponseUtil.buildAuthResponse(userByEmail)
        }

        // это новый пользовать
        // хэш пароль - нал, т.к. он только регается без пароля
        val newUserEntity = UserEntity(
            username = payload.email,
            email = payload.email,
            fullName = payload["name"] as? String,
            passwordHash = null,
            googleId = googleId,
            imageUri = payload["picture"] as? String // это абсолютная ссылка на сервера Гугла
        )
        val savedUserEntity = userRepository.save(newUserEntity)
        return authResponseUtil.buildAuthResponse(savedUserEntity)
    }
}