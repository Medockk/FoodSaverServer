package com.foodback.core.coreMedia.impl.service

import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import kotlin.math.abs

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

    override fun toRelativeUri(absoluteUri: String): String {
        if (absoluteUri.isBlank()) return absoluteUri

        // Если пришел полный URL (http://.../media/restaurants/...),
        // мы забираем только то, что идет после "/media/"
        if (absoluteUri.startsWith("http://") || absoluteUri.startsWith("https://")) {
            return if (absoluteUri.contains("/media/")) {
                absoluteUri.substringAfter("/media/") // Получим "restaurants/.../temp/abc.png"
            } else {
                absoluteUri
            }
        }

        // Если это уже относительный путь, просто убираем лидирующий слэш для порядка
        return absoluteUri.removePrefix("/")
    }
}