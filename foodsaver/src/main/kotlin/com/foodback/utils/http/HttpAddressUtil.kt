package com.foodback.utils.http

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class HttpAddressUtil(
    @Value($$"${server.address}")
    private val serverAddress: String,
    @Value($$"${server.protocol}")
    private val serverProtocol: String,
    @Value($$"${server.port}")
    private val serverPort: String,
) {

    /**
     * Return url like http://192.168.0.0:8088
     */
    fun getStartUrl(): String {
        return "$serverProtocol://$serverAddress:$serverPort"
    }
}