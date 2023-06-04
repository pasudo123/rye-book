package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.infra.ProductV2Repository
import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.ProductV2
import org.springframework.stereotype.Service

@Service
class ProductDomainCreateService(
    private val productRepository: ProductRepository,
    private val productV2Repository: ProductV2Repository
) {

    fun createOrPatch(products: List<Product>): List<Long> {
        return productRepository.saveAll(products).mapNotNull { it.id }
    }

    fun createOrPatchV2(products: List<ProductV2>): List<Long> {
        return productV2Repository.saveAll(products).mapNotNull { it.id }
    }
}
