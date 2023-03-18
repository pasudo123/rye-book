package com.github.ryebook.product.domain

import com.github.ryebook.product.infra.ProductCustomRepository
import com.github.ryebook.product.model.pub.Product
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductDomainGetService(
    private val productCustomRepository: ProductCustomRepository,
) {

    fun findAllWithPageable(type: Product.Type, pageable: Pageable): List<Product> {
        return productCustomRepository.findTypeWithPageable(type, pageable)
    }

    fun findByIdOrNull(id: Long): Product? {
        return productCustomRepository.findByIdOrNull(id)
    }
}
