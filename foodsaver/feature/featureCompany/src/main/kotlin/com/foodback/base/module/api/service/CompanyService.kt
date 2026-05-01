package com.foodback.base.module.api.service

import com.foodback.base.module.api.dto.AddCompanyRequest
import com.foodback.base.module.api.dto.CompanyResponse
import java.util.UUID

interface CompanyService {

    fun getCompanyById(id: UUID): CompanyResponse?
    fun addCompany(request: AddCompanyRequest, logoBytes: ByteArray?, logoExtension: String = "png"): CompanyResponse
}