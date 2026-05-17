package com.foodback.core.coreMedia.impl.service

import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Service
internal class MediaUriMapperServiceImpl: MediaUriMapperService {

    override fun toAbsoluteUri(relativeUri: String): String {

        if (relativeUri.isBlank() || relativeUri.startsWith("http")) {
            return relativeUri
        }

        val cleanedRelativeUri = relativeUri.removePrefix("/")

        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/media/")
            .path(cleanedRelativeUri)
            .toUriString()
    }
}