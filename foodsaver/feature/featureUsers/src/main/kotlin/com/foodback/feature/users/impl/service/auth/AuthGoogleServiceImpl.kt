package com.foodback.feature.users.impl.service.auth

import com.foodback.feature.users.api.dto.auth.AuthResponse
import com.foodback.feature.users.api.dto.auth.GoogleRequest
import com.foodback.feature.users.api.dto.proifle.CreateProfileRequest
import com.foodback.feature.users.api.service.auth.AuthGoogleService
import com.foodback.feature.users.api.service.profile.WriteProfileService
import com.foodback.feature.users.impl.entity.AuthEntity
import com.foodback.feature.users.impl.exception.auth.InvalidGoogleTokenException
import com.foodback.feature.users.impl.repository.AuthRepository
import com.foodback.feature.users.impl.util.AuthResponseUtil
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthGoogleServiceImpl(
    private val authRepository: AuthRepository,
    private val authResponseUtil: AuthResponseUtil,
    private val verifier: GoogleIdTokenVerifier,
    private val writeProfileService: WriteProfileService
): AuthGoogleService {

    @Transactional
    override fun authorizeWithGoogle(request: GoogleRequest): AuthResponse {
        val idToken = verifier.verify(request.googleId)
            ?: throw InvalidGoogleTokenException()

        val payload = idToken.payload
        val googleId = payload.subject

        // search via Google token
        val user = authRepository.findByGoogleId(googleId)
        if (user != null) {
            return authResponseUtil.buildAuthResponse(user)
        }

        // is this user already registered?
        // search by email
        val userByEmail = authRepository.findByEmail(payload.email)
        if (userByEmail != null) {
            userByEmail.apply {
                // update запись, т.к. чел входит в аккаунт с помощью гугла
                this.googleId = googleId
            }
            return authResponseUtil.buildAuthResponse(userByEmail)
        }

        // это новый пользовать
        // хэш пароль - null, т.к. он только регается без пароля
        val newAuthEntity = AuthEntity(
            username = payload.email,
            email = payload.email,
            passwordHash = null,
            googleId = googleId
        )
        val savedAuthEntity = authRepository.save(newAuthEntity)
        writeProfileService.createProfile(
            request = CreateProfileRequest(
                userId = savedAuthEntity.uid!!,
                fullName = payload["name"] as? String ?: "",
                phone = null,
                bio = null,
                imageUri = payload["picture"] as? String
            )
        )

        return authResponseUtil.buildAuthResponse(savedAuthEntity)
    }
}