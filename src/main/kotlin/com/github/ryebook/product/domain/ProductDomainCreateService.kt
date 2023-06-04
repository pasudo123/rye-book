package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.model.pub.Product
import org.springframework.stereotype.Service

@Service
class ProductDomainCreateService(
    private val productRepository: ProductRepository
) {

    fun createOrPatch(products: List<Product>): List<Long> {
        return productRepository.saveAll(products).mapNotNull { it.id }
    }
}
