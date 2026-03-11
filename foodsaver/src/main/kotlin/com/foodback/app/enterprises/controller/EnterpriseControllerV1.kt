package com.foodback.app.enterprises.controller

import com.foodback.app.enterprises.dto.response.EnterpriseImageResponseV1
import com.foodback.app.enterprises.dto.response.EnterpriseResponseV1
import com.foodback.app.enterprises.mapper.EnterpriseMappersV1
import com.foodback.app.enterprises.service.EnterpriseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/v1/enterprises")
class EnterpriseControllerV1(
    private val enterpriseService: EnterpriseService,
    private val enterpriseMappersV1: EnterpriseMappersV1
) {

    @GetMapping
    fun getNearestEnterprises(
        @RequestParam
        latitude: Double,
        @RequestParam
        longitude: Double
    ): List<EnterpriseResponseV1> {
        val enterprises = enterpriseService.getNearestEnterprise(
            latitude = latitude,
            longitude = longitude
        )
        return enterprises.map {
            enterpriseMappersV1.mapToResponse(it)
        }
    }

    @GetMapping("/{enterprise_id}")
    fun getEnterpriseById(
        @PathVariable("enterprise_id")
        enterpriseId: UUID
    ): ResponseEntity<EnterpriseResponseV1> {
        val enterprise = enterpriseService
            .getEnterpriseById(enterpriseId)

        return if (enterprise == null) {
            ResponseEntity
                .noContent()
                .build()
        } else {
            val response = enterpriseMappersV1
                .mapToResponse(enterprise)
            ResponseEntity
                .ok(response)
        }
    }

    @GetMapping("/images")
    fun getEnterpriseImages(
        @RequestParam
        enterpriseId: UUID
    ): ResponseEntity<List<EnterpriseImageResponseV1>> {
        val images = enterpriseService.getEnterpriseImages(enterpriseId)
        return if (images == null || images.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity
            .ok()
            .body(enterpriseMappersV1.mapImageUrls(images).map {
                EnterpriseImageResponseV1(it)
            })
    }

    @PostMapping("/uploadImage")
    fun uploadEnterpriseImage(
        @RequestPart("file")
        file: MultipartFile,
        @RequestParam
        enterpriseId: UUID
    ): ResponseEntity<String> {
        println("Image size is ${file.size}")
        val relativeUrl = enterpriseService
            .uploadEnterpriseImage(file, enterpriseId)

        return if (relativeUrl.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok()
            .body(enterpriseMappersV1
                .mapRelativeUrlToAbsoluteUrl(relativeUrl)
            )
    }
}