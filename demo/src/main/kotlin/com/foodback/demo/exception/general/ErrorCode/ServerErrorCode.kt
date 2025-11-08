package com.foodback.demo.exception.general.ErrorCode

typealias ErrorCode = ServerErrorCode

sealed interface ServerErrorCode {

    val code: Int
}