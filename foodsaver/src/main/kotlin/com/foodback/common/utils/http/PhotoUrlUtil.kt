package com.foodback.common.utils.http

import org.springframework.stereotype.Component

@Component
class PhotoUrlUtil(
    private val httpAddressUtil: HttpAddressUtil
) {

    fun mapRelativeUrlToAbsoluteUrl(relativeUrl: String): String {
        val photoUrl = when {
            relativeUrl.startsWith("http") -> relativeUrl
            relativeUrl.startsWith("data:image") -> relativeUrl
            relativeUrl.startsWith("/") -> {
                httpAddressUtil.getStartUrl() + relativeUrl.drop(1)
            }
            else -> httpAddressUtil.getStartUrl() + "/$relativeUrl"
        }
        println("PhotoUrl $photoUrl")
        return photoUrl
    }
}