package com.github.ryebook.product.application

import com.github.ryebook.product.api.dto.ProductDto
import com.github.ryebook.product.domain.ProductDomainPatchService
import com.github.ryebook.product.model.pub.Product
import org.springframework.statemachine.config.StateMachineFactory
import org.springframework.stereotype.Service

@Service
class ProductPatchService(
    private val productDomainPatchService: ProductDomainPatchService,
    private val productStateMachineFactory: StateMachineFactory<Product.Status, Product.Event>
) {

    fun patchStatus(request: ProductDto.RequestWithStatus) {
        productDomainPatchService.patch(request)
    }
}
