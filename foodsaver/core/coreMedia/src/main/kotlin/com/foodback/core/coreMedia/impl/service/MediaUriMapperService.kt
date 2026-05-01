package com.foodback.core.coreMedia.impl.service

import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Service
internal class MediaUriMapperService: MediaUriMapperService {

    override fun toAbsoluteUri(relativeUri: String): String {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/")
            .path(relativeUri)
            .toUriString()
    }
}