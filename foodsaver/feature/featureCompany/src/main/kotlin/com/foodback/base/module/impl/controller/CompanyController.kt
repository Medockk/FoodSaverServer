package com.foodback.base.module.impl.controller

import com.foodback.base.module.api.dto.AddCompanyRequest
import com.foodback.base.module.api.dto.CompanyResponse
import com.foodback.base.module.api.service.CompanyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/v1/company")
internal class CompanyController(
    private val companyService: CompanyService
) {

    @GetMapping
    fun getCompanyById(
        @RequestParam
        id: UUID
    ): ResponseEntity<CompanyResponse> {
        val company = companyService
            .getCompanyById(id)

        return if (company != null) ResponseEntity.ok(company)
        else ResponseEntity.noContent().build()
    }

    @PostMapping
    fun addCompany(
        @RequestPart("company")
        request: AddCompanyRequest,
        @RequestPart("logo", required = false)
        logoBytes: MultipartFile?
    ): ResponseEntity<CompanyResponse> {
        val logoExtension = logoBytes
            ?.originalFilename
            ?.substringAfterLast(".", "png")
            ?: "png"

        val response = companyService
            .addCompany(request, logoBytes?.bytes, logoExtension)

        return ResponseEntity.ok(response)
    }
}