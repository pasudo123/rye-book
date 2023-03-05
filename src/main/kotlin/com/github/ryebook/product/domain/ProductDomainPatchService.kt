package com.github.ryebook.product.domain

import com.github.ryebook.product.api.dto.ProductDto
import com.github.ryebook.product.infra.ProductRepository
import com.github.ryebook.product.model.pub.Product
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductDomainPatchService(
    private val productRepository: ProductRepository
) {

    fun patch(request: ProductDto.RequestWithStatus) {
        val currentProduct = productRepository.findByIdOrNull(request.id) ?: return
        currentProduct.update(Product.Status.valueOf(request.status))
    }
}
