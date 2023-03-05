package com.github.ryebook.product.application

import com.github.ryebook.product.api.dto.ProductDto
import com.github.ryebook.product.domain.ProductDomainPatchService
import org.springframework.stereotype.Service

@Service
class ProductPatchService(
    private val productDomainPatchService: ProductDomainPatchService
) {

    fun patchStatus(request: ProductDto.RequestWithStatus) {
        productDomainPatchService.patch(request)
    }
}
