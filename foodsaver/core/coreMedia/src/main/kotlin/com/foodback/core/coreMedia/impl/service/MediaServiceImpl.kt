package com.foodback.core.coreMedia.impl.service

import com.foodback.core.coreMedia.api.service.MediaService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
@Primary
internal class MediaServiceImpl(
    @Value($$"${app.media.root}")
    val mediaRootPath: String
): MediaService {

    private val root = Paths.get(mediaRootPath)

    override fun upload(bytes: ByteArray, folder: String, extension: String): String {
        val fileName = "${UUID.randomUUID()}.$extension"
        val targetDir = root.resolve(folder)

        // if folders does not exist
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir)
        }

        val targetFile = targetDir.resolve(fileName)
        Files.write(targetFile, bytes)

        return "$folder/$fileName"
    }

    override fun moveFromTemp(tempUri: String, newFolder: String): String {
        val fileName = tempUri.substringAfterLast("/")
        val sourcePath = Paths.get(mediaRootPath, tempUri)
        val targetFolder = Paths.get(mediaRootPath, newFolder)
        val targetPath = targetFolder.resolve(fileName)

        Files.createDirectories(targetFolder)

        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
        return "$newFolder/$fileName"
    }

    override fun delete(relativeUrl: String) {
        val path = root.resolve(relativeUrl.removePrefix("/"))
        Files.deleteIfExists(path)
    }
}