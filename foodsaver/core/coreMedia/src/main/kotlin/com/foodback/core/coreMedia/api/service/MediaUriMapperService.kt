package com.foodback.core.coreMedia.api.service

interface MediaUriMapperService {

    fun toAbsoluteUri(relativeUri: String): String
}