package com.foodback.feature.users.api.service.auth

import java.util.*

interface ReadAuthService {

    /**
     * @return email or else throw Exception чтото типа пользователь не найлен
     */
    fun getEmailByUserId(userId: UUID): String
}