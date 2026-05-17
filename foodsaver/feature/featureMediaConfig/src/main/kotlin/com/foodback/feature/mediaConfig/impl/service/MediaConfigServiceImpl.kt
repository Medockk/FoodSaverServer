package com.foodback.feature.mediaConfig.impl.service

import com.foodback.core.coreMedia.api.service.MediaService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.nio.file.Paths

@Service
internal class MediaConfigServiceImpl(
    @Value($$"${app.media.root}")
    val mediaRootPath: String
) {

    private val root = Paths.get(mediaRootPath)

    fun getFile(filename: String): Resource? {
        val file = root.resolve(filename.removePrefix("/"))
        val urlResource = UrlResource(file.toUri())

        if (!urlResource.exists() || !urlResource.isReadable) {
            return null
        }

        return urlResource
    }
}