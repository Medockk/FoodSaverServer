package com.foodback.exception.general.ErrorCode

/**
 * General sealed interface to identify all requests error
 */
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
        REFRESH_TOKEN_EXPIRED(1008),

        PASSWORD_NOT_EQUALS(1009),
        RESET_TOKEN_NOT_FOUND(1010),
        RESET_TOKEN_LINKED_TO_NULL(1011)
    }

    // Code between 2000 until 2999
    enum class UserRequest(override val code: Int = 2000) : RequestError {
        UNKNOWN_ERROR,
        USER_NOT_FOUND(2001),
        EMPTY_EMAIL(2002),
        EMAIL_NOT_FOUND(2003)
    }

    // Code between 3000 until 3999
    enum class ProductRequest(override val code: Int = 3000) : RequestError {
        PRODUCT_NOT_FOUND(3001)
    }

    // Code between 4000 until 4999
    enum class CartRequest(override val code: Int = 4000) : RequestError {
        CART_NOT_FOUND(4001)
    }

    // Code between 5000 until 5999
    enum class OrganizationRequest(override val code: Int = 5000): RequestError {
        UNKNOWN_ERROR(5000),
        ORGANIZATION_NOT_FOUND(5001)
    }

    /**
     * Special code if failed to convert [String] to [java.util.UUID]
     */
    enum class Uuid(override val code: Int = 1999) : RequestError {
        FAILED_PARSE_UUID(1999)
    }
}