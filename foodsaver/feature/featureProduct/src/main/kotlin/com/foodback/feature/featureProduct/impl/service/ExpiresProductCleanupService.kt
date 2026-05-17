package com.foodback.feature.featureProduct.impl.service

import com.foodback.feature.featureProduct.impl.repository.ProductRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
internal class ExpiresProductCleanupService(
    private val productRepository: ProductRepository
) {

    @Transactional
    @Scheduled(fixedRate = 60_000)
    fun markDeletedExpiresProducts() {
        val now = Instant.now()
        productRepository.markExpiresProductsDeleted(now)
    }
}