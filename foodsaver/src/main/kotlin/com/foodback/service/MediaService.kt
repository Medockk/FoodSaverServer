package com.foodback.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class MediaService(
    @Value($$"${app.media.root}")
    private val mediaRootPath: String
) {

    private val path = Paths.get(mediaRootPath)

    init {
        if (!Files.exists(path)) Files.createDirectories(path)
    }

    fun getMedia(mediaType: String, fileName: String): Resource {

        val filePath = path.resolve(mediaType).resolve(fileName)
        val resource: Resource = UrlResource(filePath.toUri())

        if (resource.exists() && resource.isReadable) {
            return resource
        } else {
            throw Exception("resource $fileName not found!")
        }
    }
}