package com.foodback.base.module.impl.service

import com.foodback.base.module.api.dto.AddCompanyRequest
import com.foodback.base.module.api.dto.CompanyResponse
import com.foodback.base.module.api.service.CompanyService
import com.foodback.base.module.impl.mapper.CompanyMapper
import com.foodback.base.module.impl.repository.CompanyRepository
import com.foodback.core.coreMedia.api.service.MediaService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
@Primary
internal class CompanyServiceImpl(
    private val companyRepository: CompanyRepository,
    private val companyMapper: CompanyMapper,
    private val mediaService: MediaService
) : CompanyService {

    override fun getCompanyById(id: UUID): CompanyResponse? {
        val company = companyRepository
            .findById(id)
            .getOrNull()
            ?: return null

        return companyMapper
            .toResponse(company)
    }

    @Transactional
    override fun addCompany(request: AddCompanyRequest, logoBytes: ByteArray?, logoExtension: String): CompanyResponse {

        val logoRelativeUri = logoBytes?.let { logoBytes ->
            mediaService.upload(logoBytes, "company/${request.companyName}", extension = logoExtension)
        }

        val entity = companyMapper
            .toEntity(request)
            .apply {
                this.logoUri = logoRelativeUri
            }

        val savedEntity = companyRepository.save(entity)
        return companyMapper
            .toResponse(savedEntity)
    }
}