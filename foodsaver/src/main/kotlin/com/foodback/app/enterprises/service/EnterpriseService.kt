package com.foodback.app.enterprises.service

import com.foodback.app.enterprises.dto.request.GetEnterpriseRequestV1
import com.foodback.app.enterprises.dto.response.EnterpriseImageResponseV1
import com.foodback.app.enterprises.entity.EnterprisesEntity
import com.foodback.app.enterprises.repository.EnterpriseRepository
import com.foodback.service.MediaService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class EnterpriseService(
    private val enterpriseRepository: EnterpriseRepository,
    private val mediaService: MediaService,

    @Value($$"${app.media.path.enterprises}")
    private val enterpriseUploadPath: String
) {

    fun getNearestEnterprise(
        latitude: Double,
        longitude: Double
    ): List<EnterprisesEntity> {
        val enterprises = enterpriseRepository
            .findNearestEnterprises(
                latitude = latitude,
                longitude = longitude
            )

        return enterprises
    }

    fun getEnterpriseImages(enterpriseId: UUID): List<String>? {
        val enterprises = enterpriseRepository.findById(enterpriseId)
            .getOrNull() ?: return emptyList()

        return enterprises.imageUrls
    }

    /**
     * Return relative URL
     */
    @Transactional
    fun uploadEnterpriseImage(
        file: MultipartFile,
        enterpriseId: UUID
    ): String {

        if (file.isEmpty) throw Exception()

        val enterprise = enterpriseRepository
            .findById(enterpriseId)
            .orElseThrow()

        val relativeUrl = mediaService.uploadImage(
            file = file,
            baseUploadPath = enterpriseUploadPath,
            fileUrlType = MediaService.FileUrlType.ENTERPRISES,
            enterpriseId.toString()
        )

        try {
            val existingUrls = enterprise.imageUrls ?: mutableListOf()
            existingUrls.add(relativeUrl)
            enterprise.imageUrls = existingUrls
        } catch (e: Exception) {
            mediaService.deleteImage(relativeUrl)
            throw e
        }

        return relativeUrl
    }

    fun getEnterpriseById(enterpriseId: UUID): EnterprisesEntity? {
        val enterprise = enterpriseRepository
            .findById(enterpriseId)
            .getOrNull()

        return enterprise
    }
}