package com.foodback.controller

import com.foodback.service.MediaService
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/media")
class MediaController(
    private val mediaService: MediaService
) {

    @GetMapping("/{media_type}/{filename:.+}")
    fun getMedia(
        @PathVariable("media_type")
        mediaType: String,
        @PathVariable("filename")
        fileName: String,
    ): ResponseEntity<Resource> {
        val resource = mediaService.getMedia(mediaType, fileName)
        val contentType = MediaTypeFactory.getMediaType(resource)
            .orElse(MediaType.APPLICATION_OCTET_STREAM)

        return ResponseEntity
            .ok()
            .contentType(contentType)
            .body(resource)
    }
}