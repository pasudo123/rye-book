package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.model.Product
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductDomainGetService(
    private val productRepository: ProductRepository
) {

    fun findAllWithPageable(productType: Product.ProductType, pageable: Pageable): List<Product> {
        return productRepository.findAllByProductType(productType, pageable)
    }
}