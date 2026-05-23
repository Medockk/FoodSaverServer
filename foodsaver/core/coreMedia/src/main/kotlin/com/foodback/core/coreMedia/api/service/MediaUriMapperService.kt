package com.foodback.core.coreMedia.api.service

interface MediaUriMapperService {

    fun toAbsoluteUri(relativeUri: String): String
    fun toRelativeUri(absoluteUri: String): String
}