package com.foodback.core.coreCommon.api.errorCode

/**
 * Провайдер для документирования ошибок модулей.
 * Ошибки модулей (enum class ... implement ServerErrorCode)ч
 */
interface ServerErrorCodeProvider {

    /**
     * Возвращает список ошибок данного модуля
     */
    fun getAllCodes(): List<ServerErrorCode>
}