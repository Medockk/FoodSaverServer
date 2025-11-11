package com.foodback.demo.exception.general.ErrorCode

sealed interface RequestError : ErrorCode {

    // Code 1000 until 1999
    enum class Authentication(override val code: Int) : RequestError {
        UNKNOWN_ERROR(1000),
        USERNAME_OCCUPIED(1001),
        INVALID_EMAIL_FORMAT(1002),
        WEAK_PASSWORD(1003),
        FAILED_REGISTER_USER(1004),
        FAILED_AUTHORIZE_USER(1005),

        JWT_TOKEN_EXPIRED(1006),
        JWT_TOKEN_NOT_EXPIRED(1007),
        REFRESH_TOKEN_EXPIRED(1008)
    }

    enum class UserRequest(override val code: Int = 2000) : RequestError {
        USER_NOT_FOUND(2001),
        EMPTY_EMAIL(2002),
        EMAIL_NOT_FOUND(2003)
    }

    enum class ProductRequest(override val code: Int = 3000) : RequestError {
        PRODUCT_NOT_FOUND(3001)
    }

    enum class CartRequest(override val code: Int = 4000) : RequestError {
        CART_NOT_FOUND(4001)
    }

    enum class Uuid(override val code: Int = 1999) : RequestError {
        FAILED_PARSE_UUID(1999)
    }
}