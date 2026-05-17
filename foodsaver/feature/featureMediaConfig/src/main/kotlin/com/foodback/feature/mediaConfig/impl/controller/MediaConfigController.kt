package com.foodback.feature.mediaConfig.impl.controller

import com.foodback.feature.mediaConfig.impl.service.MediaConfigServiceImpl
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/media")
internal class MediaConfigController(
    private val mediaConfigServiceImpl: MediaConfigServiceImpl
) {

    @GetMapping
    fun getFile(
        @PathVariable
        filename: String,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<Resource> {
        val resource = mediaConfigServiceImpl
            .getFile(filename)
            ?: return ResponseEntity
                .noContent()
                .build()

        val mediaType = httpServletRequest
            .servletContext
            .getMimeType(resource.file.absolutePath)
            ?: "application/octet-stream"

        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(mediaType))
            .build()
    }
}