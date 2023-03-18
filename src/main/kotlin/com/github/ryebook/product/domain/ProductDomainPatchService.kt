package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductDomainPatchService(
    private val productRepository: ProductRepository
)
