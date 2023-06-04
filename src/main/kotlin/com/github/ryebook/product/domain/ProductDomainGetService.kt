package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductCustomRepository
import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.infra.ProductV2Repository
import com.github.ryebook.product.model.pub.Product
import com.github.ryebook.product.model.pub.ProductType
import com.github.ryebook.product.model.pub.ProductV2
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductDomainGetService(
    private val productCustomRepository: ProductCustomRepository,
    private val productRepository: ProductRepository,
    private val productV2Repository: ProductV2Repository,
) {

    fun findAllWithPageable(type: ProductType, pageable: Pageable): List<Product> {
        return productCustomRepository.findTypeWithPageable(type, pageable)
    }

    fun findByIdOrNull(id: Long): Product? {
        return productRepository.findByIdOrNull(id)
    }

    fun findV2ByIdOrNull(id: Long): ProductV2? {
        return productV2Repository.findByIdOrNull(id)
    }
}
