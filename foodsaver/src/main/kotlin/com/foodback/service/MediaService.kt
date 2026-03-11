package com.foodback.service

import com.foodback.exception.auth.UserException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class MediaService(
    @Value($$"${app.media.root}")
    private val mediaRootPath: String,

    @Value($$"${server.address}")
    private val serverAddress: String,
    @Value($$"${server.port}")
    private val serverPort: String,
    @Value($$"${server.protocol}")
    private val serverProtocol: String
) {

    private val path = Paths.get(mediaRootPath)
    val baseUrl = "$serverProtocol://$serverAddress:$serverPort/"

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

    /**
     * Return a relative path
     * @param baseUploadPath Base path of upload, for example C:/foodback_data/enterprises
     * @param folders The folders, includes in path after [baseUploadPath]
     */
    fun uploadImage(
        file: MultipartFile,
        baseUploadPath: String,
        fileUrlType: FileUrlType,
        vararg folders: String = emptyArray()
    ): String {

        if (file.isEmpty) throw UserException("File is empty!")

        val fileUploadPath = Paths.get(baseUploadPath, *folders)
        val uploadDirs = fileUploadPath.toFile()
        if (!uploadDirs.exists()) uploadDirs.mkdirs()

        val extensions = file.contentType?.substringAfter("/") ?: "jpg"
        val fileName = "${UUID.randomUUID()}.$extensions"

        val filePath = fileUploadPath.resolve(fileName)
        Files.write(filePath, file.bytes)

        val folderPathForUrl = folders.joinToString("/")
        val relativeUrl = "media/${fileUrlType.value}" + if (folderPathForUrl.isEmpty()) "/$fileName"
        else "/$folderPathForUrl/$fileName"

        return relativeUrl
    }

    fun deleteImage(relativeUrl: String) {
        val cleanUrl = relativeUrl.removePrefix("/")
            .removePrefix("media")
            .removePrefix("/")

        println("Clean url $cleanUrl")
        val path = Paths.get(mediaRootPath, cleanUrl)
        println("Path $path")
        val isDeleted = Files.deleteIfExists(path)
        println("IsDeleted: $isDeleted")
    }

    enum class FileUrlType(val value: String) {
        AVATARS("avatars"),
        OFFERS("offers"),
        PRODUCTS("products"),
        ENTERPRISES("enterprises")
    }
}